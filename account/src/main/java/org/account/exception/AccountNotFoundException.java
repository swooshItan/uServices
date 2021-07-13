package org.account.exception;


public class AccountNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private static final String MSG_FORMAT = "Account with account number, %s not found";


    public AccountNotFoundException(String accountNumber) {
        super(String.format(MSG_FORMAT, accountNumber));
    }
}
