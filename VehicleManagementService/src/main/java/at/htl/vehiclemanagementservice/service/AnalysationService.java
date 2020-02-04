package at.htl.vehiclemanagementservice.service;

import at.htl.vehiclemanagementservice.domain.VehicleRepository;
import at.htl.vehiclemanagementservice.handler.buffers.RawEnvironmentalDataAggregationsBuffer;
import at.htl.vehiclemanagementservice.model.AnalysationResult;
import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import at.htl.vehiclemanagementservice.model.Vehicle;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.Tuple4;
import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.compute.ComputeJob;
import org.apache.ignite.compute.ComputeJobAdapter;
import org.apache.ignite.compute.ComputeJobResult;
import org.apache.ignite.compute.ComputeTaskSplitAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class AnalysationService {

    private final Ignite ignite;
    private final VehicleRepository repository;
    private final RawEnvironmentalDataAggregationsBuffer buffer;

    @Autowired
    public AnalysationService(Ignite ignite, VehicleRepository repository, RawEnvironmentalDataAggregationsBuffer buffer) {
        this.ignite = ignite;
        this.repository = repository;
        this.buffer = buffer;
    }

    @Scheduled(fixedDelay = 600000)
    public void analyzeRawEnvironmentalDataAggregationsForEach() {
        repository.findAll().forEach(v -> analyzeRawEnvironmentalDataAggregations(v.getId()));
    }

    public void analyzeRawEnvironmentalDataAggregations(Long vehicleId) {
        var cache = ignite.<UUID, AnalysationResult>cache("analysationResultCache");
        var compute = ignite.compute();

        var analysationResult = compute.execute(AnalyzeRawEnvironmentalDataAggregationsTask.class, Tuple.of(vehicleId, repository, buffer));

        cache.putAsync(analysationResult.getId(), analysationResult);
    }

    private static class AnalyzeRawEnvironmentalDataAggregationsTask extends ComputeTaskSplitAdapter<Tuple3<Long, VehicleRepository, RawEnvironmentalDataAggregationsBuffer>, AnalysationResult> {

        @Override
        public Collection<? extends ComputeJob> split(int gridSize, Tuple3<Long, VehicleRepository, RawEnvironmentalDataAggregationsBuffer> tuple) {
            var rawEnvironmentalDataAggregations = StreamSupport.stream(tuple._3.spliterator(), false)
                    .filter(rawEnvironmentalDataAggregation -> rawEnvironmentalDataAggregation.getVehicleId().longValue() == tuple._1)
                    .collect(Collectors.toList());

            var jobs = new ArrayList<ComputeJob>(rawEnvironmentalDataAggregations.size());

            for (var rawEnvironmentalDataAggregation : rawEnvironmentalDataAggregations) {
                jobs.add(new ComputeJobAdapter() {
                    @Override public Object execute() {
                        return Tuple.of(tuple._2.findById(tuple._1), rawEnvironmentalDataAggregation);
                    }
                });
            }

            return jobs;
        }

        @Override
        public AnalysationResult reduce(List<ComputeJobResult> results) {

            List<RawEnvironmentalDataAggregation> rawEnvironmentalDataAggregations = new ArrayList<>();

            var data = results.get(0).<Tuple2<Optional<Vehicle>, RawEnvironmentalDataAggregation>>getData();

            for (ComputeJobResult res : results) {
                rawEnvironmentalDataAggregations.add(res.<Tuple2<Optional<Vehicle>, RawEnvironmentalDataAggregation>>getData()._2);
            }

            return new AnalysationResult(UUID.randomUUID(),
                    data._1.map(Vehicle::getId).orElseThrow(() -> new RuntimeException(new InstanceNotFoundException())),
                    data._1.orElseThrow(() -> new RuntimeException(new InstanceNotFoundException())),
                    rawEnvironmentalDataAggregations.stream()
                            .mapToDouble(RawEnvironmentalDataAggregation::getTemperature)
                            .average()
                            .orElseThrow(() -> new RuntimeException(new MathUnsupportedOperationException())),
                    rawEnvironmentalDataAggregations.stream()
                            .mapToDouble(RawEnvironmentalDataAggregation::getHumidity)
                            .average()
                            .orElseThrow(() -> new RuntimeException(new MathUnsupportedOperationException())),
                    rawEnvironmentalDataAggregations.stream()
                            .mapToDouble(RawEnvironmentalDataAggregation::getLightIntensity)
                            .average()
                            .orElseThrow(() -> new RuntimeException(new MathUnsupportedOperationException())),
                    rawEnvironmentalDataAggregations.get(0).getLatitude(),
                    rawEnvironmentalDataAggregations.get(0).getLongitude(),
                    LocalDateTime.now());
        }
    }
}
