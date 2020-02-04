package at.htl.vehiclemanagementservice.handler.listeners;

import at.htl.vehiclemanagementservice.domain.RawEnvironmentalDataAggregationsStream;
import at.htl.vehiclemanagementservice.handler.buffers.RawEnvironmentalDataAggregationsBuffer;
import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RawEnvironmentalDataAggregationsListener {

    private final RawEnvironmentalDataAggregationsBuffer buffer;

    @Autowired
    public RawEnvironmentalDataAggregationsListener(RawEnvironmentalDataAggregationsBuffer buffer) {
        this.buffer = buffer;
    }

    @StreamListener
    public void process(@Input(RawEnvironmentalDataAggregationsStream.INPUT) KStream<?, RawEnvironmentalDataAggregation> input) {
        input.foreach((k, v) -> buffer.buffer(v));
        // Linear Regression Model here, maybe in ForkJoin job?
        // Save result in Ignite
    }
}
