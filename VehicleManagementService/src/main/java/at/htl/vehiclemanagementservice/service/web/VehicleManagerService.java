package at.htl.vehiclemanagementservice.service.web;

import at.htl.vehiclemanagementservice.domain.AnalysationResultRepository;
import at.htl.vehiclemanagementservice.domain.VehicleRepository;
import at.htl.vehiclemanagementservice.dto.Gps;
import at.htl.vehiclemanagementservice.dto.vehicle.EnvironmentalData;
import at.htl.vehiclemanagementservice.dto.vehicle.GetAllVehiclesResponse;
import at.htl.vehiclemanagementservice.dto.vehicle.PredictedEnvironmentalData;
import at.htl.vehiclemanagementservice.dto.vehicle.SimplifiedVehicle;
import at.htl.vehiclemanagementservice.handler.buffers.RawEnvironmentalDataAggregationsBuffer;
import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import at.htl.vehiclemanagementservice.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.management.InstanceNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class VehicleManagerService {

    private final VehicleRepository vehicleRepository;
    private final AnalysationResultRepository analysationResultRepository;
    private final RawEnvironmentalDataAggregationsBuffer aggregationsBuffer;

    @Autowired
    public VehicleManagerService(VehicleRepository vehicleRepository, AnalysationResultRepository analysationResultRepository, RawEnvironmentalDataAggregationsBuffer aggregationsBuffer) {
        this.vehicleRepository = vehicleRepository;
        this.analysationResultRepository = analysationResultRepository;
        this.aggregationsBuffer = aggregationsBuffer;
    }

    public Mono<GetAllVehiclesResponse> getAll() {
        return Mono.just(new GetAllVehiclesResponse(StreamSupport.stream(vehicleRepository.findAll().spliterator(), false)
                .map(v -> new SimplifiedVehicle(v.getId(), v.getName()))
                .collect(Collectors.toList())));
    }

    public Mono<SimplifiedVehicle> get(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(new InstanceNotFoundException()));

        return Mono.just(new SimplifiedVehicle(vehicle.getId(), vehicle.getName()));
    }

    public Mono<Gps> getGps(Long id, LocalDateTime dt) {
        return Flux.fromIterable(aggregationsBuffer)
                .filter(rawEnvironmentalDataAggregation -> rawEnvironmentalDataAggregation.getMeasuredAt().isAfter(Collections.max(StreamSupport.stream(aggregationsBuffer.spliterator(), false)
                        .filter(r -> r.getVehicleId().longValue() == id)
                        .filter(r -> dt.isAfter(r.getMeasuredAt().minusDays(1)) && dt.isBefore(r.getMeasuredAt().plusDays(1)))
                        .map(RawEnvironmentalDataAggregation::getMeasuredAt)
                        .collect(Collectors.toList()))
                        .minusDays(1)))
                .next()
                .map(rawEnvironmentalDataAggregation -> new Gps(rawEnvironmentalDataAggregation.getLatitude(), rawEnvironmentalDataAggregation.getLongitude()));
    }

    public Mono<Void> update(Long id, SimplifiedVehicle simplifiedVehicle) {
        var vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(new InstanceNotFoundException()));
        vehicle.setName(simplifiedVehicle.getName());

        vehicleRepository.save(id, vehicle);

        return Mono.empty();
    }

    public Mono<EnvironmentalData> getEnvironmentalData(Long id) {
        return Flux.fromIterable(aggregationsBuffer)
                .filter(rawEnvironmentalDataAggregation -> rawEnvironmentalDataAggregation.getMeasuredAt().isAfter(Collections.max(StreamSupport.stream(aggregationsBuffer.spliterator(), false)
                        .filter(r -> r.getVehicleId().longValue() == id)
                        .map(RawEnvironmentalDataAggregation::getMeasuredAt)
                        .collect(Collectors.toList()))
                        .minusDays(1)))
                .next()
                .map(rawEnvironmentalDataAggregation -> new EnvironmentalData(rawEnvironmentalDataAggregation.getTemperature(),
                        rawEnvironmentalDataAggregation.getHumidity(),
                        rawEnvironmentalDataAggregation.getLightIntensity(),
                        new Gps(rawEnvironmentalDataAggregation.getLatitude(), rawEnvironmentalDataAggregation.getLongitude())));
    }

    public Flux<EnvironmentalData> getEnvironmentalData(Long id, Integer days) {
        var resultList = new ArrayList<EnvironmentalData>();

        for (int i = 0; i < days; i++) {
            final int index = i;
            var environmentalData = StreamSupport.stream(aggregationsBuffer.spliterator(), false)
                    .filter(r -> r.getVehicleId().longValue() == id)
                    .filter(rawEnvironmentalDataAggregation -> {
                        if (index == 0) {
                            return rawEnvironmentalDataAggregation.getMeasuredAt().isAfter(LocalDateTime.now().minusDays(1)) &&
                                    rawEnvironmentalDataAggregation.getMeasuredAt().isBefore(LocalDateTime.now().plusDays(1));
                        }

                        if (index == 1) {
                            return rawEnvironmentalDataAggregation.getMeasuredAt().isAfter(LocalDateTime.now().minusDays(2)) &&
                                    rawEnvironmentalDataAggregation.getMeasuredAt().isBefore(LocalDateTime.now());
                        }

                        return rawEnvironmentalDataAggregation.getMeasuredAt().isAfter(LocalDateTime.now().minusDays(index + 1)) &&
                                rawEnvironmentalDataAggregation.getMeasuredAt().isBefore(LocalDateTime.now().minusDays(index - 1));
                    })
                    .findFirst()
                    .map(rawEnvironmentalDataAggregation -> new EnvironmentalData(rawEnvironmentalDataAggregation.getTemperature(),
                            rawEnvironmentalDataAggregation.getHumidity(),
                            rawEnvironmentalDataAggregation.getLightIntensity(),
                            new Gps(rawEnvironmentalDataAggregation.getLatitude(), rawEnvironmentalDataAggregation.getLongitude())))
                    .orElse(new EnvironmentalData(-1.0, -1.0, -1.0, new Gps(-1.0, -1.0)));

            resultList.add(environmentalData);
        }

        return Flux.fromIterable(resultList);
    }

    public Flux<PredictedEnvironmentalData> getPredictedEnvironmentalData(Long id, Integer days) {
        var predictedEnvironmentalData = StreamSupport.stream(analysationResultRepository.findAll().spliterator(), false)
                .filter(analysationResult -> analysationResult.getMeasuredAt().isAfter(Collections.max(StreamSupport.stream(aggregationsBuffer.spliterator(), false)
                        .filter(r -> r.getVehicleId().longValue() == id)
                        .map(RawEnvironmentalDataAggregation::getMeasuredAt)
                        .collect(Collectors.toList()))
                        .minusDays(1)))
                .findFirst()
                .map(analysationResult -> new PredictedEnvironmentalData(analysationResult.getTemperature(),
                        analysationResult.getHumidity(),
                        analysationResult.getLightIntensity()))
                .orElse(new PredictedEnvironmentalData(-1.0, -1.0, -1.0));

        return Flux.range(0, days).map(ignored -> predictedEnvironmentalData);
    }
}
