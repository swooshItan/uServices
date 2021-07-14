This repository contains 5 applications. The purpose of this repository is to provide a simple reference on the development of cloud-native applications using opensource frameworks such as Spring Framework, Spring Boot and Spring Cloud, deployed as docker containers managed in kubernetes platform.

A brief description and information on the corresponding opensource frameworks, libraries and languages used for each application are indicated below.
* customer -

* account

* mgmt-ui

* api-gateway

* keycloak

The high level interactions between these applications are as follows:-

![image](https://user-images.githubusercontent.com/36339970/125591091-0e66235b-1e31-4b05-ad6c-b3670f14f85c.png)

| Request Flow                    | Description             |
| client -> api-gateway/keycloak  | All HTTP requests to mgmt-ui, account and customer apps would have to go through api-gateway first |
