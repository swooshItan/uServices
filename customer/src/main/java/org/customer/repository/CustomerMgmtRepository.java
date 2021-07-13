package org.customer.repository;

import java.util.List;

import org.customer.domain.Customer;

public interface CustomerMgmtRepository {

	public Customer save(Customer customer);
	
	public void delete(Long id);

	public Customer findById(Long id);

	public List<Customer> findAll();

}
