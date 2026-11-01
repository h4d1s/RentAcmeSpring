package com.pixellabs.user;

//import java.io.IOException;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.UUID;
//
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
//import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//import com.pixellabs.user.application.services.AppUserDetailsService;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	// private final AppUserDetailsService appUserDetailsService;
	
//    public SecurityConfig(AppUserDetailsService appUserDetailsService) {
//        this.appUserDetailsService = appUserDetailsService;
//    }

//	@Bean
//	@Order(2)
//	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
//			throws Exception {
//		http
//				.authorizeHttpRequests(authorizeRequests ->
//						authorizeRequests
//								.requestMatchers(
//										"/css/**",
//										"/js/**",
//										"/auth/login",
//										"/auth/signup",
//										"/oauth2/**"
//								).permitAll()
//								.anyRequest().authenticated()
//				)
//				.formLogin(formLogin ->
//						formLogin
//								.loginPage("/auth/login")
//								.failureHandler(authenticationFailureHandler())
//								.permitAll()
//				);
//
//		return http.build();
//	}

//	@Bean
//	public RegisteredClientRepository registeredClientRepository() {
//		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
//				.clientId("oidc-client")
//				.clientSecret(passwordEncoder().encode("secret"))
//				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//				.redirectUri("http://localhost:5235/login/oauth2/code/oidc-client")
//				.postLogoutRedirectUri("http://localhost:5235/")
//				.scope(OidcScopes.OPENID)
//				.scope(OidcScopes.PROFILE)
//				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//				.build();
//
//		return new InMemoryRegisteredClientRepository(oidcClient);
//	}
//
//	@Bean
//	public JWKSource<SecurityContext> jwkSource() {
//		KeyPair keyPair = generateRsaKey();
//		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//		RSAKey rsaKey = new RSAKey.Builder(publicKey)
//				.privateKey(privateKey)
//				.keyID(UUID.randomUUID().toString())
//				.build();
//		JWKSet jwkSet = new JWKSet(rsaKey);
//		return new ImmutableJWKSet<>(jwkSet);
//	}
//
//	private static KeyPair generateRsaKey() {
//		KeyPair keyPair;
//		try {
//			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//			keyPairGenerator.initialize(2048);
//			keyPair = keyPairGenerator.generateKeyPair();
//		}
//		catch (Exception ex) {
//			throw new IllegalStateException(ex);
//		}
//		return keyPair;
//	}
//
//	@Bean
//	public AuthorizationServerSettings authorizationServerSettings() {
//		return AuthorizationServerSettings.builder().build();
//	}
//
//	@Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
	
//	@Bean
//    public AuthenticationFailureHandler authenticationFailureHandler() {
//        return new SimpleUrlAuthenticationFailureHandler() {
//            @Override
//            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                                AuthenticationException exception) throws IOException, ServletException {
//                // Log error details
//                logger.error("Authentication failed: " + exception.getMessage(), exception);
//                super.onAuthenticationFailure(request, response, exception);
//            }
//        };
//    }
}
