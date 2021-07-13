package org.customer.service;

import java.util.List;

import org.customer.domain.Customer;
import org.customer.exception.CustomerNotFoundException;

public interface CustomerMgmtService {

    public Customer createCustomer(Customer customer);
    
    public void deleteCustomer(Long id);

    public Customer getCustomer(Long id) throws CustomerNotFoundException;

    public List<Customer> getCustomers();

}
