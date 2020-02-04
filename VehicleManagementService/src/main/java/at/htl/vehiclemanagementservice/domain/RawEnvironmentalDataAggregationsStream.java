package at.htl.vehiclemanagementservice.domain;

import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface RawEnvironmentalDataAggregationsStream {

    String INPUT = "rawEnvironmentalDataAggregations-in";
    String OUTPUT = "rawEnvironmentalDataAggregations-out";

    @Input(INPUT)
    KStream<?, RawEnvironmentalDataAggregation> inboundRawEnvironmentalDataAggregations();

    @Output(OUTPUT)
    MessageChannel outboundRawEnvironmentalDataAggregations();
}
