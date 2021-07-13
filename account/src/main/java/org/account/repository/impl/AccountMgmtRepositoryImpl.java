package org.account.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.account.domain.Account;
import org.account.repository.AccountMgmtRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AccountMgmtRepositoryImpl implements AccountMgmtRepository {
	
	@PersistenceContext(unitName = "accountPU")
	private EntityManager em;

	@Override
	public Account findByAccNum(String accountNumber) {
		return em.find(Account.class, accountNumber);
	}

	@Override
	public Account findByAccNumWithLock(String accountNumber) {
		return em.find(Account.class, accountNumber, LockModeType.PESSIMISTIC_WRITE);
	}

	@Override
	public void save(Account account) {
		em.persist(account);
	}
}
