package org.account.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

import io.swagger.annotations.ApiModelProperty;

public class OpenAccountRequest {
  
    @ApiModelProperty(name = "customerId", value ="Id of customer to open account for")
    @Digits(integer=10, fraction = 0)
    private Long customerId;

    @ApiModelProperty(name = "amt", value = "Initial balance to open account with")
    @Digits(integer=10, fraction=2)
    private BigDecimal amt;


    public Long getCustomerId() {
    	return customerId;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public String toString() {
        return new StringBuilder().append("OpenAccountRequest[customerId=").append(customerId)
        	.append(", amt=").append(amt).append("]").toString();
    }
}
