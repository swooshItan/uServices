package org.customer.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.customer.domain.Customer;
import org.customer.exception.CustomerNotFoundException;
import org.customer.metrics.CustomerMgmtMetricsManager;
import org.customer.repository.CustomerMgmtRepository;
import org.customer.service.CustomerMgmtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerMgmtServiceImpl implements CustomerMgmtService {

    @Autowired
    private CustomerMgmtMetricsManager customerMgmtMetricsManager;

    @Autowired
    private CustomerMgmtRepository customerMgmtRepository;


    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
    	Customer newCustomer = customerMgmtRepository.save(customer);   	
    	customerMgmtMetricsManager.incCreatedCount();
    	return newCustomer;
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
    	customerMgmtRepository.delete(id);
    }

    @Override
    public Customer getCustomer(Long id) throws CustomerNotFoundException {
        Customer retCustomer = customerMgmtRepository.findById(id);
        if (retCustomer == null) {
            throw new CustomerNotFoundException(id);
        }

        customerMgmtMetricsManager.incGetCount();
        return retCustomer;
    }

    @Override
    public List<Customer> getCustomers() {
    	List<Customer> retCustomers = customerMgmtRepository.findAll();
    	
    	customerMgmtMetricsManager.incGetCount();
    	return retCustomers;
    }
}
