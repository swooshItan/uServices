package org.account.service.impl;

import javax.transaction.Transactional;

import org.account.domain.Account;
import org.account.domain.OpenAccountRequest;
import org.account.domain.Customer;
import org.account.domain.TransferRequest;
import org.account.exception.AccountNotFoundException;
import org.account.exception.InvalidParamException;
import org.account.metrics.AccountMgmtMetricsManager;
import org.account.repository.AccountMgmtRepository;
import org.account.service.AccountMgmtService;
import org.account.serviceclient.CustomerMgmtServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountMgmtServiceImpl implements AccountMgmtService {

	@Autowired
	private CustomerMgmtServiceClient customerMgmtServiceClient;

    @Autowired
    private AccountMgmtRepository accountMgmtRepository;

    @Autowired
    private AccountMgmtMetricsManager accountMgmtMetricsManager;


    @Override
    @Transactional
    public Account openAccount(OpenAccountRequest openAccountRequest) throws InvalidParamException {
    	// verify if it is for a valid customer
    	Customer customer = customerMgmtServiceClient.getCustomer(openAccountRequest.getCustomerId());
    	if (customer == null) {
            throw new InvalidParamException("customerId", openAccountRequest.getCustomerId().toString(), "customer not found");
    	}

    	// open account
    	Account newAccount = Account.open(openAccountRequest);
        accountMgmtRepository.save(newAccount);
        accountMgmtMetricsManager.incOpenedCount();

        return newAccount;
    }

    @Override
    public Account getAccount(String accountNumber) throws AccountNotFoundException {
        Account retAccount = accountMgmtRepository.findByAccNum(accountNumber);
        if (retAccount == null) {
            throw new AccountNotFoundException(accountNumber);
        }

        accountMgmtMetricsManager.incGetCount();
        return retAccount;
    }

    @Override
    @Transactional
    public void transfer(TransferRequest transferRequest) throws InvalidParamException {
        Account srcAccount = accountMgmtRepository.findByAccNumWithLock(transferRequest.getSrcAccountNumber());
        if (srcAccount == null) {
            throw new InvalidParamException("srcAccountNumber", transferRequest.getSrcAccountNumber(), "account not found");
        }

        Account destAccount = accountMgmtRepository.findByAccNumWithLock(transferRequest.getDestAccountNumber());
        if (destAccount == null) {
            throw new InvalidParamException("destAccountNumber", transferRequest.getDestAccountNumber(), "account not found");
        }

        srcAccount.withdraw(transferRequest.getAmt());
        destAccount.deposit(transferRequest.getAmt());

        accountMgmtRepository.save(srcAccount);
        accountMgmtRepository.save(destAccount);
        accountMgmtMetricsManager.incTransferCount();
    }
}
