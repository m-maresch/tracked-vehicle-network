package at.htl.vehiclemanagementservice.service;

import at.htl.vehiclemanagementservice.domain.VehicleRepository;
import at.htl.vehiclemanagementservice.handler.buffers.GpsDataMeasuredEventBuffer;
import at.htl.vehiclemanagementservice.handler.buffers.HumidityMeasuredEventBuffer;
import at.htl.vehiclemanagementservice.handler.buffers.LightIntensityMeasuredEventBuffer;
import at.htl.vehiclemanagementservice.handler.buffers.TemperatureMeasuredEventBuffer;
import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import io.vavr.collection.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
public class DataAggregatorService {

    private final VehicleRepository repository;

    private final TemperatureMeasuredEventBuffer temperatureMeasuredEventBuffer;
    private final HumidityMeasuredEventBuffer humidityMeasuredEventBuffer;
    private final LightIntensityMeasuredEventBuffer lightIntensityMeasuredEventBuffer;
    private final GpsDataMeasuredEventBuffer gpsDataMeasuredEventBuffer;

    private final RawEnvironmentalDataAggregationsService environmentalDataAggregationsService;

    @Autowired
    public DataAggregatorService(VehicleRepository repository,
                                 TemperatureMeasuredEventBuffer temperatureMeasuredEventBuffer,
                                 HumidityMeasuredEventBuffer humidityMeasuredEventBuffer,
                                 LightIntensityMeasuredEventBuffer lightIntensityMeasuredEventBuffer,
                                 GpsDataMeasuredEventBuffer gpsDataMeasuredEventBuffer,
                                 RawEnvironmentalDataAggregationsService environmentalDataAggregationsService) {
        this.repository = repository;
        this.temperatureMeasuredEventBuffer = temperatureMeasuredEventBuffer;
        this.humidityMeasuredEventBuffer = humidityMeasuredEventBuffer;
        this.lightIntensityMeasuredEventBuffer = lightIntensityMeasuredEventBuffer;
        this.gpsDataMeasuredEventBuffer = gpsDataMeasuredEventBuffer;
        this.environmentalDataAggregationsService = environmentalDataAggregationsService;
    }

    @Scheduled(fixedDelay = 1000)
    public void aggregateForEach() {
        repository.findAll().forEach(v -> aggregate(v.getId()));
    }

    public void aggregate(Long vehicleId) {
        var temperatureMeasuredEventStream = StreamSupport.stream(temperatureMeasuredEventBuffer.spliterator(),false);
        var humidityMeasuredEventStream = StreamSupport.stream(humidityMeasuredEventBuffer.spliterator(),false);
        var lightIntensityMeasuredEventStream = StreamSupport.stream(lightIntensityMeasuredEventBuffer.spliterator(),false);
        var gpsDataMeasuredEventStream = StreamSupport.stream(gpsDataMeasuredEventBuffer.spliterator(),false);

        var environmentalDataMeasuredEvents = List.of(temperatureMeasuredEventStream, humidityMeasuredEventStream, lightIntensityMeasuredEventStream)
                .map(stream -> stream.filter(event -> event.getId().longValue() == vehicleId)
                        .findFirst())
                .filter(Optional::isPresent)
                .map(Optional::get);
        var gpsDataMeasuredEventOptional = gpsDataMeasuredEventStream.filter(event -> event.getId().longValue() == vehicleId).findFirst();

        if (environmentalDataMeasuredEvents.size() == 3 && gpsDataMeasuredEventOptional.isPresent()) {
            var temperatureMeasuredEvent = environmentalDataMeasuredEvents.get(0);
            var humidityMeasuredEvent = environmentalDataMeasuredEvents.get(1);
            var lightIntensityMeasuredEvent = environmentalDataMeasuredEvents.get(2);
            var gpsDataMeasuredEvent = gpsDataMeasuredEventOptional.get();

            var aggregate = new RawEnvironmentalDataAggregation(UUID.randomUUID(), vehicleId,
                    temperatureMeasuredEvent.getValue(),
                    humidityMeasuredEvent.getValue(),
                    lightIntensityMeasuredEvent.getValue(),
                    gpsDataMeasuredEvent.getLatitude(),
                    gpsDataMeasuredEvent.getLongitude(),
                    LocalDateTime.now());

            environmentalDataAggregationsService.send(aggregate);
        }
    }
}
