# Overview
This repository contains 5 applications. This repository provides a simple reference on the development of cloud-native applications using opensource frameworks such as Spring Framework, Spring Boot and Spring Cloud, deployed as docker containers managed in kubernetes platform.

A brief description and information on the corresponding opensource frameworks, libraries and languages used for each application are indicated below.

  * customer - provides REST APIs for CRUD functions for customer information (needs to be authenticated). The data is stored in in-memory database, H2.
    
    Uses
      * spring-boot-actuator/micrometer to provide endpoints for liveness/readiness checks for kubernetes deployment, and endpoint for monitoring with prometheus compatible format.
      * spring-boot-web for implement the REST controllers
      * spring-boot-data-jpa for implement the respository layer to store/retrieve the data in in-memory db, H2. 
      * spring-boot-outh2-resource-server for verify that all requests are authenticated with valid JWT token.
      * spring-cloud-contract-verifier to test and verify the REST API contracts as a producer for account application
      * spring-cloud-sleuth for distributed tracing
      * swagger-annotations for generating swagger json format file to document the REST APIs.
      * junit, rest-assured for implement unit and integration tests

  * account - provides REST APIs for CRUD functions for account information. The data is stored in in-memory database, H2.

    Uses
      * spring-boot-actuator/micrometer to provide endpoints for liveness/readiness checks for kubernetes deployment, and endpoint for monitoring with prometheus compatible format.
      * spring-boot-web for implement the REST controllers
      * spring-boot-data-jpa for implement the respository layer to store/retrieve the data in in-memory db, H2.
      * spring-boot-outh2-resource-server for verify that all requests are authenticated with valid JWT token. (pending)
      * spring-cloud-contract-verifier to test and verify the REST API contracts as a consumer of customer application
      * spring-cloud-sleuth for distributed tracing
      * spring-cloud-circuitbreaker used when making REST API call to customer application
      * swagger-annotations for generating swagger json format file to document the REST APIs.
      * junit, rest-assured for implement unit and integration tests

  * api-gateway - a proxy which all requests need to go to before forwarding to downstream applications. It handles the check if client is authenticated, and send the JWT token to downsteam applications.

    Uses
      * spring-boot-actuator/micrometer to provide endpoints for liveness/readiness checks for kubernetes deployment, and endpoint for monitoring with prometheus compatible format.

  * mgmt-ui - provide the UI of customer management to call customer REST APIs (needs to be authenticated). Simple web application using angularJS.      

  * keycloak - handles the authentication and other IAM related functions. Uses keycloak with in-memory database from a docker image published in dockerhub.

# Request Flows
The high level interactions between these applications are as follows:-

![image](https://user-images.githubusercontent.com/36339970/125591091-0e66235b-1e31-4b05-ad6c-b3670f14f85c.png)

  * client -> api-gateway/keycloak

    All HTTP requests to mgmt-ui, account and customer applications would have to go through api-gateway first. api-gateway would check if client has been authenticated before forwarding the requests to downstream applications. If client has not been authenticated yet, client would be redirected to keycloak to display the login page for user to enter their credentials to login. After login is successful, client would be redirected back to the api-gateway before requests is further forwarded to downstream applications.
    
  * api-gateway -> keycloak

    After authentication is successfully done by keycloak, it would send a response to redirect the client back to api-gateway. api-gateway would use the information (authorization code, etc) provided by keycloak during the redirect to make a request to keycloak to get the access token (JWT). 

  * api-gateway -> mgmt-ui

    After authentication is successful, api-gateway would forward requests to downstream application, mgmt-ui for the UI webpages, and other web related files.

  * api-gateway -> customer/account

    After authentication is successful, api-gateway would forward requests to downstream application, customer/account for the REST API calls. This is triggered via the UI.
    The JWT access token is passed to these applications from api-gateway.

  * customer -> keycloak

    customer application can make a request to keycloak to request for the public key to verify the signature of the JWT token passed as part of the request header.

  * account -> customer

    account application can make a REST API call to customer application. Currently, not working yet (work-in-progress) after adding security to customer application.
    

# Pending
- handle CSRF
- add sec to account app. whether propagate token to downstream customer app or using grant-type = client_credentials?

