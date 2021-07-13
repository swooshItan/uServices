# uServices
This repository contains 5 applications
	- customer
	- account
	- mgmt-ui
	- api-gateway
	- keycloak
	
The high level interactions between these applications are as follows:-


																				Browser Client
																					|					|
																					|					|
																					V					V
																	api-gateway	---> keycloak
																	|		|		|
												  ---------		|		---------
													|						|						|
													V						V						V
											account			mgmt-ui			customer

