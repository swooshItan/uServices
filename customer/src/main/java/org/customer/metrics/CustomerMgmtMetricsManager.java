package org.customer.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerMgmtMetricsManager {

    private static final String CREATED_COUNT = "customer_created_count";

    private static final String GET_COUNT = "customer_get_count";

    private static final String TAG_TYPE = "type";

    private static final String TAG_TYPE_COUNTER = "counter";

    private Counter createdCounter;

    private Counter getCounter;


    @Autowired
    public CustomerMgmtMetricsManager(MeterRegistry registry) {
        createdCounter = registry.counter(CREATED_COUNT, TAG_TYPE, TAG_TYPE_COUNTER);
        getCounter = registry.counter(GET_COUNT, TAG_TYPE, TAG_TYPE_COUNTER);
    }

    public void incCreatedCount() {
        createdCounter.increment();
    }

    public void incGetCount() {
        getCounter.increment();
    }
}
