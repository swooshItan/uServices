package org.customer;

import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.customer.domain.Customer;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CustomerMgmtIntegrationTest {
	
	private static JwtHelper helper;

	private static String jwt;


    @BeforeAll
    public static void init() throws IOException, JoseException {
    	Properties testProps = new Properties();
    	testProps.load(CustomerMgmtIntegrationTest.class.getResourceAsStream("/test.properties"));

    	// init target addr to send request
        RestAssured.port = Integer.parseInt(testProps.getProperty("customerService.port"));
        RestAssured.baseURI = "http://" + testProps.getProperty("customerService.host");

    	// init and start mock server
        int jwkSetUriPort = Integer.parseInt(testProps.getProperty("jwkSetUriMock.port"));
        String jwkSetUriUri = testProps.getProperty("jwkSetUriMock.uri");

    	helper = new JwtHelper();
    	helper.initAndStartJwkSetUriServer(jwkSetUriPort, jwkSetUriUri);
    	
    	jwt = helper.generateJwt("issuer", "testwebuser");
    }
    
    @AfterAll
    public static void destroy() {
    	helper.stopJwkSetUriServer();
    }

    @Test
    public void testCreateAndGetCustomer() {
        // create account
    	String expName = "Tester";
    	Long id = createCustomer(expName);

        // get customer and verify results
        Customer customer = getCustomer(id);
        assertEquals(expName, customer.getName());
    }

    @Test
    public void testCreateCustomerNameEmpty() {
    	withTokenSetInHeader().contentType(ContentType.JSON).body("{ \"name\" : \"\" }")
			.when().post("/customers").then().log().all()
			.statusCode(400);
    }

    @Test
    public void testGetCustomerNotFound() {
    	withTokenSetInHeader().contentType(ContentType.JSON).when().get("/customer/" + UUID.randomUUID().toString()).then().log().all().statusCode(404);
    }

    private Long createCustomer(String name) {
    	return withTokenSetInHeader().contentType(ContentType.JSON).body("{ \"name\" : \"" + name + "\" }")
    			.when().post("/customers").then().log().all()
    			.statusCode(201).extract().as(Customer.class).getId();
    }

    private Customer getCustomer(Long id) {
    	return withTokenSetInHeader().contentType(ContentType.JSON)
    			.when().get("/customers/" + id).then().log().all()
    			.statusCode(200).extract().as(Customer.class);
    }
    
    private RequestSpecification withTokenSetInHeader() {
    	return with().header("Authorization", "Bearer " + jwt);
    }
}
