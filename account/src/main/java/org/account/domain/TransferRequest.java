package org.account.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class TransferRequest {

    @ApiModelProperty(name = "srcAccountNumber", value = "Source Account Number to transfer from")
    @NotBlank(message = "Source account number is mandatory")
    private String srcAccountNumber;

    @ApiModelProperty(name = "destAccountNumber", value = "Destination Account Number to transfer to")
    @NotBlank(message = "Destination account number is mandatory")
    private String destAccountNumber;

    @ApiModelProperty(name = "amt", value = "Amount to transfer")
    @Digits(integer=10, fraction=2)
    private BigDecimal amt;


    public String getSrcAccountNumber() {
        return srcAccountNumber;
    }

    public String getDestAccountNumber() {
        return destAccountNumber;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public String toString() {
        return new StringBuilder().append("TransferRequest[srcAccountNumber=").append(srcAccountNumber).append(", destAccountNumber=").append(destAccountNumber)
                .append(", amt=").append(amt).append("]").toString();
    }
}
