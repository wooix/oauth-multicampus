package com.multi.oauth20server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.PasswordLookup;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 여기는 매우 중요하다. ~ 눈부릅뜨고 gpt한테도 물어봐 formLogin작동이 어떻게 되는지 확인해보고
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
		return http.formLogin(Customizer.withDefaults()).build();
	}

	private JWKSet buildJWKSet() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException {
		KeyStore keyStore = KeyStore.getInstance("pkcs12");
		try (FileInputStream fis = new FileInputStream("src/main/resources/oauth2-sample.pfx")) {
			keyStore.load(fis, "123456".toCharArray());
			return JWKSet.load(keyStore, new PasswordLookup() {
				@Override
				public char[] lookupPassword(String name) {
					return "123456".toCharArray();
				}
			});
		}
	}

	// 387page 비교하며 볼것

	@Bean
	JWKSource<SecurityContext> jwkSource() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException {
		JWKSet jwkSet = buildJWKSet();
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	AuthorizationServerSettings providerSettings() {
		return AuthorizationServerSettings.builder().issuer("http://tfactory.com").build();
	}

	@Bean
	TokenSettings tokenSettings() {
		// @formatter:off
		return TokenSettings.builder()
				.accessTokenTimeToLive(Duration.ofMinutes(30))
				.refreshTokenTimeToLive(Duration.ofDays(1))
				.reuseRefreshTokens(false)
				.build();
		// @formatter:on
	}

	@Bean
	ClientSettings clientSettings() {
		return ClientSettings.builder().requireAuthorizationConsent(true).build();
	}

	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(
				jdbcTemplate);
		// @formatter:off
		RegisteredClient client1 = jdbcRegisteredClientRepository.findByClientId("client1");
	    if (client1 == null) {
	      RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
	        .clientId("client1")
	        .clientSecret(passwordEncoder().encode("1234"))
	        .clientSettings(clientSettings())
	        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)	
	        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)		//리프레시 토큰 사용하려면 배이직 인증 방식 사용해야되
	        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
	        .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
	        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
	        .redirectUri("https://oidcdebugger.com/debug")
	        .redirectUri("http://jcornor.com:8000/callback")
	        .redirectUri("https://oauth.pstmn.io/v1/callback")								// postman oauth callback
	        .redirectUri("http://server:8000/swagger-ui/oauth2-redirect.html")				// swagger oauth callback
	        .scope(OidcScopes.OPENID).scope("profiles").scope("contacts").scope("messages")	// access_token : "JWT" (권한증명) --> id_token : "JWT (ID/PW대용) 로 변경??
	        .tokenSettings(tokenSettings()).build();
	      jdbcRegisteredClientRepository.save(registeredClient);
	    }
		// @formatter:on

		// oauth parameter이름과 db column간의 이름 mapping!!
		// 너무 복잡다 그냥 기본값만 쓰도로고 하자
		JdbcRegisteredClientRepository.RegisteredClientParametersMapper mapper = new JdbcRegisteredClientRepository.RegisteredClientParametersMapper();
		jdbcRegisteredClientRepository.setRegisteredClientParametersMapper(mapper);
		return jdbcRegisteredClientRepository;
	}

	// jdbc client repository를 AuthorizationService에 설정함
	@Bean
	OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
	}

	// 사용자가 client 앱을 승인(Consent)한 정보를 jdbc로 테이블에 저장
	@Bean
	OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

}
