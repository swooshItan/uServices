package org.account.domain;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({"accountNumber", "balance"})
public class Account {

    @Id
    @ApiModelProperty(name = "accountNumber", value = "Account number")
    private String accountNumber;
    
    @ApiModelProperty(name = "customerId", value ="Customer id")
    private Long customerId;

    @ApiModelProperty(name = "balance", value = "Account balance")
    private BigDecimal balance;


    public String getAccountNumber() {
        return accountNumber;
    }
    
    public Long getCustomerId() {
    	return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public static Account open(OpenAccountRequest openAccountRequest) {
    	Account account = new Account();
    	account.accountNumber = UUID.randomUUID().toString();
    	account.customerId = openAccountRequest.getCustomerId();
    	account.balance = openAccountRequest.getAmt();
    	return account;
    }

    public void deposit(BigDecimal amt) {
        balance = balance.add(amt);
    }

    public void withdraw(BigDecimal amt) {
        balance = balance.subtract(amt);
    }

    public String toString() {
        return new StringBuilder().append("Account[accountNumber=").append(accountNumber).append(", customerId=").append(customerId)
        	.append(", balance=").append(balance).append("]").toString();
    }
}
