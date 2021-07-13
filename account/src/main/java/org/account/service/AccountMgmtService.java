package org.account.service;

import org.account.domain.Account;
import org.account.domain.OpenAccountRequest;
import org.account.domain.TransferRequest;
import org.account.exception.AccountNotFoundException;
import org.account.exception.InvalidParamException;

public interface AccountMgmtService {

    public Account openAccount(OpenAccountRequest openAccountRequest) throws InvalidParamException;

    public Account getAccount(String accountNumber) throws AccountNotFoundException;

    public void transfer(TransferRequest transferRequest) throws InvalidParamException;

}
