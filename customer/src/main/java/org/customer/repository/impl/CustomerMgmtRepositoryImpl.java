package org.customer.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.customer.domain.Customer;
import org.customer.repository.CustomerMgmtRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CustomerMgmtRepositoryImpl implements CustomerMgmtRepository {
	
	@PersistenceContext(unitName = "customerPU")
	private EntityManager em;

	
	@Override
	public Customer save(Customer customer) {
		em.persist(customer);
		em.flush();
		return customer;
	}
	
	@Override
	public void delete(Long id) {
		Customer customer = em.find(Customer.class, id);
		if (customer != null) {
			em.remove(customer);
		}
	}

	@Override
	public Customer findById(Long id) {
		return em.find(Customer.class, id);
	}
	
	@Override
	public List<Customer> findAll() {
		return em.createQuery("select c from Customer c", Customer.class).getResultList();
	}
}
