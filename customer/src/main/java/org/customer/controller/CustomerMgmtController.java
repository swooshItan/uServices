package org.customer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;

import org.customer.domain.Customer;
import org.customer.exception.CustomerNotFoundException;
import org.customer.service.CustomerMgmtService;
import org.customer.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
@Api(value = "customers")
public class CustomerMgmtController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMgmtController.class);

    @Autowired
    private CustomerMgmtService customerMgmtService;


    @PostMapping(consumes = "application/json")
    @ApiOperation(value = "Create customer", notes = "Create customer")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Customer created"), @ApiResponse(code = 400, message = "Invalid input parameters"),
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<Customer> createCustomer(
    		@Valid @RequestBody @ApiParam(value = "Create customer information", required = true) Customer customer,
    		@AuthenticationPrincipal Jwt jwt) {

        Customer newCustomer = customerMgmtService.createCustomer(customer);

        LOGGER.debug("customer created, {} by {}", newCustomer, jwt != null ? jwt.getClaimAsString("preferred_username") : "unknown");

        return ResponseEntity.created(Util.newURI("/customers/" + newCustomer.getId())).body(newCustomer);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "Delete customer", notes = "Delete customer")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Delete customer successful"), 
    		@ApiResponse(code = 400, message = "Invalid input parameters"),
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<Void> deleteCustomer(
    		@Digits(integer=10, fraction = 0) @PathVariable("id") @ApiParam(value = "Customer id", required = true) Long id) {

        customerMgmtService.deleteCustomer(id);

        LOGGER.debug("customer deleted for id, {}", id);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @ApiOperation(value = "Get customer", notes = "Get customer")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Get customer successful"), 
    		@ApiResponse(code = 400, message = "Invalid input parameters"),
            @ApiResponse(code = 404, message = "Customer not found"), 
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<Customer> getCustomer(
    		@Digits(integer=10, fraction = 0) @PathVariable("id") @ApiParam(value = "Customer id", required = true) Long id)
            throws CustomerNotFoundException {

        Customer customer = customerMgmtService.getCustomer(id);

        LOGGER.debug("customer retrieved, {}", customer);

        return ResponseEntity.ok(customer);
    }

    @GetMapping(value = "", produces = "application/json")
    @ApiOperation(value = "Get customers", notes = "Get customers")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message = "Get customers successful"), 
            @ApiResponse(code = 500, message = "Unexpected error occurred")})
    public ResponseEntity<List<Customer>> getCustomers() {

        return ResponseEntity.ok(customerMgmtService.getCustomers());
    }
}
