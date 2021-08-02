package org.account;

import java.util.UUID;

import org.jose4j.json.JsonUtil;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

public class JwtHelper {

	/**
	 * Generated using
	 * 		RsaJsonWebKey webKey = RsaJwkGenerator.generateJwk(2048);
	 * 		webKey.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
	 * 		webKey.setKeyId("WZ4q6W9ybwPhylqeW9jQ0i7BMYnE9JbievOCXDLhoUc");
	 * 		webKey.setUse("sig");
	 * 		webKey.toJson(OutputControlLevel.INCLUDE_PRIVATE);
	 */
	private static final String WEB_KEY_JSON = "{\"kty\":\"RSA\",\"kid\":\"WZ4q6W9ybwPhylqeW9jQ0i7BMYnE9JbievOCXDLhoUc\",\"use\":\"sig\",\"alg\":\"RS256\","
			+ "\"n\":\"patoOsbLsf3CC-Z3x_8fo7fVCSxy7gBd5F0-UnqXsJ869axIafSWF-6RxDn9uovyjMCuZBHTMD89ky4DgzU0H1Wal8I5fm6gAcngMYhb_zDqeT_z6uI8t284jNhPy8cbmYo8FU1q0-Y1kMU-A1M4ffZMdD7IbxDK0HUcOGmPv1UOjsA7-wZdY359H6G301jbu2wI5TJ3y5PvtqCalSltpNQIZzvNKBwMOyJuPErGd3Ky9vLgm_PAQ7gTbh8416CG0nSYCuc6ZpeZ82IWgCry0-w3u13zOuqCsvIDcqx9xN003Y3Ykapn-elOCtXlaIrT4YuNhtOw4aIdXYMafNXP3w\","
			+ "\"e\":\"AQAB\","
			+ "\"d\":\"POThHeGeU6E7ZxZiZr_OyGW0x3dwEy1NVbg0RSrMgN42VMDrqQ5-8PL-30qVibERCU11jFDE-JH2NxgNsHeo3e-gMDHy4hcVdzLV1j8R2Z4IQWMRa_-WY7Jc7eBAjF12NpyJ8yaOoq6Wd80YOy6msB4NAhlloPViJvc01UNv09p8LeaqK-6dE80wwa5mWg8BiYywTpuMnhcs2phlBgAu1ugzOhFt_c6uOc7XwbabP8zxVvJNYd8cPgBrGfhiGrNvX9hiyTiimPFvW4vlfIw72iudgWou7PDwRI4ZnHXTqxfatapbAMNILazIdVn993hu7gaJsIoOTRBeP9nPr0fqaQ\","
			+ "\"p\":\"_Q5RMWfgtUCMtLNeUJLDOES7YlHXpVh96m6_eh-sPXSkB8jXjaDpyWoJo6vJxZ9rSD06U3dVlL2qOMbetdk7c-2gvQHEVVE2dFcyIGYOkB4aIE0jVuoD7tSc1oatY-9jh1vPKz5aj28njjotPURSv6NzCEpCMZHtj9BNkO5AJhU\","
			+ "\"q\":\"p5jTMRpWtkxYbPdRvTjP7DZBLBqcAdyQUdeL8VgXgiJRimX0MZffvXCD_je4TtU-GnAOetdPWLA0luER23RBiROcfdQETQh_t-T98vkNbcTXt399rPT8q2Gg6gcntM_hO1qanJYUdhzhzLcTABkvB_Eji0A0oJ2rnLFv0ng37yM\","
			+ "\"dp\":\"EhUS0i_1C56YL1Vd2O4TyxKGPUlR2nvPS5AsWIKo4R9TlUbRuEoWSpEsLSjaWyfAF8JgR5SQZfeDkVkMkvv51pfVU4wrxljuRjPw3gXM5plwDnzUmKJSM5-3sVCgVTLG7uYtkI59FCDCN9SKZQVb0FvSnXYI8tfVnOAqmr-bj90\","
			+ "\"dq\":\"EyI1ajOxlppctgP-RvYHdtYy1pArI2NE7rMG_QSrmmXEnWJKYKcQGhaQWyONXSKnNml4nbmbC2sCdf84BK_fE4EzqoHcA9dmjV4NSAH68H86iW56sJY_imlY757lf5UEB7yuThYsO9fMR4zXFHnLjsKO5kTsPTIC956iP4sOB7U\","
			+ "\"qi\":\"1bcB_Ev8DnoZvid3Q9AptkQ_9ZMZAcsICJL7oXc2lCUqE5EJVCRKdnUKl461qqJQZ3fDHcLJXAw4JGLWW5vgBeTdk99D6iits3GEW2nI9PGei3YNyDwgWIYGuvs1xyDTpoG7gQan4U9hr2FLul9HXlWBZvphmWlcrl1yrG_wKd8\"}";

	private RsaJsonWebKey webKey;

	private WireMockServer jwkSetUriMockServer;

	
	public JwtHelper() throws JoseException {
		this.webKey = new RsaJsonWebKey(JsonUtil.parseJson(WEB_KEY_JSON), null);
	}

    public String generateJwt(String issuer, String username) throws JoseException {
        JwtBuilder jwtBuilder = new JwtBuilder();
        jwtBuilder.setWebKey(this.webKey);
        jwtBuilder.setIssuer(issuer);
        jwtBuilder.setUsername(username);
        return jwtBuilder.build().getCompactSerialization();
    }

    public void initAndStartJwkSetUriServer(int jwkSetUriPort, String jwkSetUriUri) {
        String webKeyJson = "{ \"keys\": [ " + this.webKey.toJson() + " ] }";

        this.jwkSetUriMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(jwkSetUriPort));
        this.jwkSetUriMockServer.addStubMapping(WireMock
       		.get(WireMock
       			.urlEqualTo(jwkSetUriUri))
   				.willReturn(WireMock.aResponse()
   					.withHeader("Content-Type", "application/json")
   					.withBody(webKeyJson))
   				.build()
        );
        this.jwkSetUriMockServer.start();
    }

    public void stopJwkSetUriServer() {
        this.jwkSetUriMockServer.stop();
    }

    /**
     * For generate jwt
     */
    private class JwtBuilder {
    	
    	private RsaJsonWebKey webKey;

    	private String issuer;
    	
    	private String username;

    	
    	public JwtBuilder setWebKey(RsaJsonWebKey webKey) {
    		this.webKey = webKey;
    		return this;
    	}

    	public JwtBuilder setIssuer(String issuer) {
    		this.issuer = issuer;
    		return this;
    	}
    	
    	public JwtBuilder setUsername(String username) {
    		this.username = username;
    		return this;
    	}
    	
    	public JsonWebSignature build() throws JoseException {
    		JwtClaims claims = new JwtClaims();
    		claims.setJwtId(UUID.randomUUID().toString());
    		claims.setIssuer(issuer);
    		claims.setIssuedAtToNow();
    		claims.setClaim("preferred_username", username);
    		claims.setClaim("azp", "testwebclient");
    		claims.setExpirationTimeMinutesInTheFuture(3);

    		JsonWebSignature webSignature = new JsonWebSignature();
    		webSignature.setPayload(claims.toJson());
    		webSignature.setKey(webKey.getPrivateKey());
    		webSignature.setAlgorithmHeaderValue(webKey.getAlgorithm());
    		webSignature.setKeyIdHeaderValue(webKey.getKeyId());
    		webSignature.setHeader("typ", "JWT");
    		return webSignature;
    	}
    }
}
