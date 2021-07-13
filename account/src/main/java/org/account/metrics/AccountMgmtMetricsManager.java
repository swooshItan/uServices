package org.account.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountMgmtMetricsManager {

    private static final String OPENED_COUNT = "account_opened_count";

    private static final String GET_COUNT = "account_get_count";

    private static final String TRANSFER_COUNT = "account_transfer_count";

    private static final String TAG_TYPE = "type";

    private static final String TAG_TYPE_COUNTER = "counter";

    private Counter openedCounter;

    private Counter getCounter;

    private Counter transferCounter;


    @Autowired
    public AccountMgmtMetricsManager(MeterRegistry registry) {
        openedCounter = registry.counter(OPENED_COUNT, TAG_TYPE, TAG_TYPE_COUNTER);
        getCounter = registry.counter(GET_COUNT, TAG_TYPE, TAG_TYPE_COUNTER);
        transferCounter = registry.counter(TRANSFER_COUNT, TAG_TYPE, TAG_TYPE_COUNTER);
    }

    public void incOpenedCount() {
        openedCounter.increment();
    }

    public void incGetCount() {
        getCounter.increment();
    }

    public void incTransferCount() {
        transferCounter.increment();
    }
}
