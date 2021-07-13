package org.account.serviceclient.impl;

import org.account.domain.Customer;
import org.account.serviceclient.CustomerMgmtServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class CustomerMgmtServiceClientImpl implements CustomerMgmtServiceClient {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMgmtServiceClientImpl.class);

	private static final String CB_NAME = "customerService";

    private WebClient webClient;

    private ReactiveCircuitBreakerFactory<?, ?> cbFactory;


    @Autowired
    public CustomerMgmtServiceClientImpl(
    		WebClient.Builder webClientBuilder, 
    		@Value("${customerService.baseUrl}") String custServiceBaseUrl,
    		ReactiveCircuitBreakerFactory<?, ?> cbFactory) {

    	this.webClient = webClientBuilder.baseUrl(custServiceBaseUrl).build();
    	this.cbFactory = cbFactory;
    }

    @Override
    public Customer getCustomer(long customerId) {
    	return cbFactory.create(CB_NAME).run(
    		webClient.get()
    			.uri("/customers/{customerId}", customerId)
    			.retrieve()
    			.bodyToMono(Customer.class)
    			.doOnError(ex -> LOGGER.error("error calling {}, msg = {}", CB_NAME, ex.getMessage(), ex))
    			.onErrorResume(
    				ex -> (ex instanceof WebClientResponseException && ((WebClientResponseException)ex).getStatusCode() == HttpStatus.NOT_FOUND), 
    				ex -> Mono.empty())
    	).block();
    }
}
