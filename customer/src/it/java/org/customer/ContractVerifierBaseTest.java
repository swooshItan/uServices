package org.customer;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

import io.restassured.module.mockmvc.RestAssuredMockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes =  ContractVerifierTestConfiguration.class)
public class ContractVerifierBaseTest {

	@Autowired
	private WebApplicationContext webApplicationContext;


	@BeforeEach
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

		// setup test data for contract tests
		given().spec(given().header("Content-Type", "application/json"))
			.log().ifValidationFails()
			.body("{ \"name\" : \"customer1\" }")
			.post("/customers")
			.then().statusCode(201);
	}
}
