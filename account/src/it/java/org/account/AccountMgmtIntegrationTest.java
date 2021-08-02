package org.account;

import static io.restassured.RestAssured.with;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import org.account.domain.Account;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

public class AccountMgmtIntegrationTest {

	private static final Properties TEST_PROPS;
	static {
		TEST_PROPS = new Properties();
		try {
			TEST_PROPS.load(AccountMgmtIntegrationTest.class.getResourceAsStream("/test.properties"));
		} catch (IOException e) {
			throw new IllegalStateException("unable to continue test since failed to load test.properties");
		}
	}

	private static JwtHelper helper;

	private static String jwt;


    @BeforeAll
    public static void init() throws IOException, JoseException {
    	// init target addr to send request
    	RestAssured.port = Integer.parseInt(TEST_PROPS.getProperty("accountService.port"));
        RestAssured.baseURI = "http://" + TEST_PROPS.getProperty("accountService.host");

    	// init and start mock server
        int jwkSetUriPort = Integer.parseInt(TEST_PROPS.getProperty("jwkSetUriMock.port"));
        String jwkSetUriUri = TEST_PROPS.getProperty("jwkSetUriMock.uri");

    	helper = new JwtHelper();
    	helper.initAndStartJwkSetUriServer(jwkSetUriPort, jwkSetUriUri);
    	
    	jwt = helper.generateJwt("issuer", "testwebuser");
    }
    
    @AfterAll
    public static void destroy() {
    	helper.stopJwkSetUriServer();
    }
	
	@RegisterExtension
	static StubRunnerExtension stubRunnerExt = new StubRunnerExtension()
		.stubsMode(StubRunnerProperties.StubsMode.CLASSPATH)
		.withGenerateStubs(true)	// need to set to true if not stub mappings would not be created
		.downloadStub("org", "customer", "+", "stubs")
		.withPort(Integer.parseInt(TEST_PROPS.getProperty("customerServiceMock.port")));


    @Test
    public void testAccountTransfer() {
        // open accounts
    	long customerId1 = 1;
        String accountNumber1 = openAccount(customerId1, 100);
        String accountNumber2 = openAccount(customerId1, 100);

        // get accounts, and verify balances before transfer
        Account account1 = getAccount(accountNumber1);
        Account account2 = getAccount(accountNumber2);
        assertEquals(account1.getBalance().intValue(), 100);
        assertEquals(account2.getBalance().intValue(), 100);

        // perform transfer
        transferAmt(accountNumber1, accountNumber2, 50);

        // get accounts, and verify balances after transfer
        account1 = getAccount(accountNumber1);
        account2 = getAccount(accountNumber2);
        assertEquals(account1.getBalance().intValue(), 50);
        assertEquals(account2.getBalance().intValue(), 150);
    }

    @Test
    public void testOpenAccountCustomerNotFound() {
    	withTokenSetInHeader().contentType(ContentType.JSON).body("{ \"customerId\" : 9999, \"amt\" : 200 }")
        	.when().post("/accounts").then().log().all()
            .statusCode(400);
    }

    @Test
    public void testGetAccountNotFound() {
    	withTokenSetInHeader().contentType(ContentType.JSON)
    			.when().get("/accounts/" + UUID.randomUUID().toString()).then().log().all()
    			.statusCode(404);
    }

    private String openAccount(long customerId, int amt) {
        return withTokenSetInHeader().contentType(ContentType.JSON).body("{ \"customerId\" : " + customerId + ", \"amt\" : " + amt + " }")
        		.when().post("/accounts").then().log().all()
                .statusCode(201).extract().as(Account.class).getAccountNumber();
    }
    
    private Account getAccount(String accountNumber) {
    	return withTokenSetInHeader().contentType(ContentType.JSON)
    			.when().get("/accounts/" + accountNumber).then().log().all()
    			.statusCode(200).extract().as(Account.class);
    }
    
    private void transferAmt(String srcAccountNumber, String destAccountNumber, int amt) {
    	withTokenSetInHeader().contentType(ContentType.JSON).body("{ \"srcAccountNumber\" : \"" + srcAccountNumber + 
        		"\", \"destAccountNumber\" : \"" + destAccountNumber + "\", \"amt\" : " + amt +" }")
        		.when().post("/accounts/transfer").then().log().all()
        		.statusCode(200);
    }

    private RequestSpecification withTokenSetInHeader() {
    	return with().header("Authorization", "Bearer " + jwt);
    }
}
