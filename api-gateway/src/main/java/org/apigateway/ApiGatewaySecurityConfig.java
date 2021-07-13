package org.apigateway;

import static org.springframework.security.config.Customizer.withDefaults;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

@Configuration
@EnableWebFluxSecurity
public class ApiGatewaySecurityConfig {
	
	@Autowired
	@Value("${logout.redirectUri}")
	private String logoutRedirectUrl;

	@Autowired
	private ReactiveClientRegistrationRepository clientRegistrationRepository;

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
		httpSecurity
			.authorizeExchange(authorize -> authorize.anyExchange().authenticated())
			.oauth2Login(withDefaults())
			.logout().logoutUrl("/signout").logoutSuccessHandler(newOidcServerLogoutSuccessHandler());
		httpSecurity.csrf().disable();
		return httpSecurity.build();
	}

	private ServerLogoutSuccessHandler newOidcServerLogoutSuccessHandler() {
		OidcClientInitiatedServerLogoutSuccessHandler serverLogoutSuccessHandler = 
			new OidcClientInitiatedServerLogoutSuccessHandler(this.clientRegistrationRepository);

	    serverLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}" + logoutRedirectUrl);
	    serverLogoutSuccessHandler.setLogoutSuccessUrl(URI.create(logoutRedirectUrl));
	    return serverLogoutSuccessHandler;
	}
}
