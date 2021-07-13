package org.customer.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomerMgmtLogInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMgmtLogInterceptor.class);


    @Around("execution(* org.customer.controller.CustomerMgmtController.*(..))")
    public Object interceptMethod(ProceedingJoinPoint jp) throws Throwable {

        String methodName = jp.getSignature().getName();
        Object returnObj = jp.proceed();

        LOGGER.debug("called REST API, {} with response, {}", methodName, returnObj);

        return returnObj;
    }
}
