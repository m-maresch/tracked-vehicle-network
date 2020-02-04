package at.htl.vehiclemanagementservice.service;

import at.htl.vehiclemanagementservice.domain.RawEnvironmentalDataAggregationsStream;
import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class RawEnvironmentalDataAggregationsService {

    private final RawEnvironmentalDataAggregationsStream stream;

    @Autowired
    public RawEnvironmentalDataAggregationsService(RawEnvironmentalDataAggregationsStream stream) {
        this.stream = stream;
    }

    public void send(final RawEnvironmentalDataAggregation aggregate) {
        MessageChannel messageChannel = stream.outboundRawEnvironmentalDataAggregations();
        messageChannel.send(MessageBuilder
                .withPayload(aggregate)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }
}
