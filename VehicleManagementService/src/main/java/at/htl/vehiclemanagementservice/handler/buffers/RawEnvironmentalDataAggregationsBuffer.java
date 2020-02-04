package at.htl.vehiclemanagementservice.handler.buffers;

import at.htl.vehiclemanagementservice.model.RawEnvironmentalDataAggregation;
import org.apache.ignite.Ignite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RawEnvironmentalDataAggregationsBuffer extends Buffer<RawEnvironmentalDataAggregation> {

    @Autowired
    public RawEnvironmentalDataAggregationsBuffer(Ignite ignite) {
        super(5000, "rawEnvironmentalDataAggregationsQueue", ignite);
    }
}
