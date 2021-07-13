package org.account.serviceclient;

import org.account.domain.Customer;

public interface CustomerMgmtServiceClient {

    public Customer getCustomer(long customerId);

}
