package org.account.repository;

import org.account.domain.Account;

public interface AccountMgmtRepository {

	public Account findByAccNum(String accountNumber);

	public Account findByAccNumWithLock(String accountNumber);

	public void save(Account account);

}
