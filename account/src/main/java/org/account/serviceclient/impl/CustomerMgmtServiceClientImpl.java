package org.account.serviceclient.impl;

import java.util.Arrays;

import org.account.domain.Customer;
import org.account.serviceclient.CustomerMgmtServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class CustomerMgmtServiceClientImpl implements CustomerMgmtServiceClient {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMgmtServiceClientImpl.class);

	private static final String CB_NAME = "customerService";

    private ReactiveCircuitBreakerFactory<?, ?> cbFactory;

    private WebClient webClient;


    @Autowired
    public CustomerMgmtServiceClientImpl(
    		ReactiveCircuitBreakerFactory<?, ?> cbFactory,
    		WebClient.Builder webClientBuilder, 
    		@Value("${customerService.baseUrl}") String custServiceBaseUrl) {

    	this.cbFactory = cbFactory;

    	this.webClient = webClientBuilder
			.baseUrl(custServiceBaseUrl)
			.filters(exchangeFilterFunctions -> {
				exchangeFilterFunctions.add(addAuthorizationHeaderFilter());
				exchangeFilterFunctions.add(logRequestFilter());
			})
			.build();
    }

    /**
     * Propagate the incoming JWT to downstream application
     */
    private ExchangeFilterFunction addAuthorizationHeaderFilter() {
    	return ExchangeFilterFunction.ofRequestProcessor(request -> {

    		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    		if (principal instanceof Jwt) {
        		request = ClientRequest.from(request).headers(headers -> {
        			headers.setBearerAuth(((Jwt)principal).getTokenValue());
        		}).build();
    		}

    		return Mono.just(request);
    	});
    }

    /**
     * Log request headers to verify that authorization header is set for debugging purpose
     */
    private ExchangeFilterFunction logRequestFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {

        	if (LOGGER.isDebugEnabled()) {
	        	StringBuilder sb = new StringBuilder("request headers: ").append(request.url().toString()).append("\n");

	        	request.headers().forEach((name, values) -> 
	        		sb.append("\t").append(name).append("=").append(Arrays.toString(values.toArray())).append("\n")
	        	);	
	        	LOGGER.debug(sb.toString());
        	}

        	return Mono.just(request);
        });
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
