package org.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.account.domain.Account;
import org.account.domain.OpenAccountRequest;
import org.account.domain.TransferRequest;
import org.account.exception.AccountNotFoundException;
import org.account.exception.InvalidParamException;
import org.account.service.AccountMgmtService;
import org.account.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@Api(value = "accounts")
public class AccountMgmtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountMgmtController.class);

    @Autowired
    private AccountMgmtService accountMgmtService;


    @PostMapping(consumes = "application/json")
    @ApiOperation(value = "Open account", notes = "Open account")
    @ApiResponses(value = {
    		@ApiResponse(code = 201, message = "Account opened"),
    		@ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<Account> openAccount(
            @Valid @RequestBody @ApiParam(value = "Ooen account request information", required = true) OpenAccountRequest openAccountRequest)
            throws InvalidParamException {

        Account newAccount = accountMgmtService.openAccount(openAccountRequest);

        LOGGER.debug("account opened, {}", newAccount);

        return ResponseEntity.created(Util.newURI("/accounts/" + newAccount.getAccountNumber())).body(newAccount);
    }


    @GetMapping(value = "/{accountNumber}", produces = "application/json")
    @ApiOperation(value = "Get account", notes = "Get account")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Get account successful"),
    		@ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 404, message = "Account not found"),
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<Account> getAccount(
    		@NotBlank(message = "Account number is mandatory") @PathVariable("accountNumber") @ApiParam(value = "Account number", required = true) String accountNumber)
    		throws AccountNotFoundException {

        Account account = accountMgmtService.getAccount(accountNumber);

        LOGGER.debug("account retrieved, {}", account);

        return ResponseEntity.ok(account);
    }


    @PostMapping(value = "/transfer", consumes = "application/json")
    @ApiOperation(value = "Transfer amt from src account to dest account", notes = "Transfer amt from src account to dest account")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Transfer successful"),
    		@ApiResponse(code = 400, message = "Invalid parameters"),
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<Void> transfer(
    		@Valid @RequestBody @ApiParam(value = "Transfer request information", required = true) TransferRequest transferRequest)
    		throws InvalidParamException {

        accountMgmtService.transfer(transferRequest);

        LOGGER.debug("transfer done, {}", transferRequest);

        return ResponseEntity.ok().build();
    }
}
