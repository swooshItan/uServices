package org.customer.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;

//@Component
//@Order(1)
public class CustomerMgmtFilter implements Filter {

	/**
	 * Not used. Needed only for testing directly with UI since UI and the services have different host/port to prevent CORS issue.
	 */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://192.168.99.100:30555");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,HEAD,POST,DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type");

        chain.doFilter(request, response);
    }
}
