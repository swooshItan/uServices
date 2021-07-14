# Overview
This repository contains 5 applications. The purpose of this repository is to provide a simple reference on the development of cloud-native applications using opensource frameworks such as Spring Framework, Spring Boot and Spring Cloud, deployed as docker containers managed in kubernetes platform.

A brief description and information on the corresponding opensource frameworks, libraries and languages used for each application are indicated below.

  * customer -

  * account

  * mgmt-ui

  * api-gateway

  * keycloak

The high level interactions between these applications are as follows:-

![image](https://user-images.githubusercontent.com/36339970/125591091-0e66235b-1e31-4b05-ad6c-b3670f14f85c.png)

# Request Flows

  * client -> api-gateway/keycloak

    All HTTP requests to mgmt-ui, account and customer applications would have to go through api-gateway first. api-gateway would check if client has been authenticated before forwarding the requests to downstream applications. If client has not been authenticated yet, client would be redirected to keycloak to display the login page for user to enter their credentials to login. After login is successful, client would be redirected back to the api-gateway before requests is further forwarded to mgmt-ui.
    
  * api-gateway -> keycloak

  * api-gateway -> mgmt-ui

  * api-gateway -> customer/account

  * customer -> keycloak

  * account -> customer
