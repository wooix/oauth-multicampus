package com.multi.oauth20server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

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

	/**
	 * * AuthorizationServer 보안 필터 적용 - SpringSecurityConfig의
	 * defaultSecurityFilterChain Bean보다 로드 우선순위를 높여야 함 - OAuth2.0 기본 보안 설정 적용 -
	 * oidc debugger를 이용할 수 있도록 설정 추가 - 사용자 인증은 formLoing을 이용하도록 설정함
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
		return http.formLogin(Customizer.withDefaults()).build();
	}

	/**
	 * * Json Web Key 셋을 생성함. - JWT의 서명을 생성할 때는 Private Key 사용, 서명 검증시에는 Public Key
	 * 사용 - 리소스 서버로 Public Key를 전달하기 위해 JWK 설정이 필요함 -
	 * https://www.baeldung.com/spring-security-oauth2-jws-jwk
	 */
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

	// 보안 컨텍스트에서 사용하는 JWKSource를 생성
	// 앞에서의 buildJWKSet() 메서드의 리턴값을 JWKSet으로 설정함
	@Bean
	JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, KeyStoreException, CertificateException,
			FileNotFoundException, IOException {
		JWKSet jwkSet = buildJWKSet();
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	// JWT 디코더로 jwkSource 지정
	@Bean
	JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	// JWT 토큰 발급자를 지정함
	@Bean
	AuthorizationServerSettings providerSettings() {
		return AuthorizationServerSettings.builder().issuer("http://tfactory.com").build();
	}

	// JWT 토큰 설정 : AT 유효 기간(60분), RT 유효 기간(30일), RT 사용
	@Bean
	TokenSettings tokenSettings() {
		return TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(60))
				.refreshTokenTimeToLive(Duration.ofDays(30)).reuseRefreshTokens(true).build();
	}

	// 사용자가 Client 앱을 승인하기 위해 승인 페이지(Consent Page)를 띄우도록 설정. 묵시적 승인(X)
	@Bean
	ClientSettings clientSettings() {
		return ClientSettings.builder().requireAuthorizationConsent(true).build();
	}

	/**
	 * * 클라이언트 앱 정보를 jdbc로 조회하도록 설정 - 별도의 서비스, 컨트롤러를 이용해 앱을 등록하는 화면을 만들 수 있음
	 * registeredClient -> jdbcRegisteredClientRepository.save(registeredClient);
	 */
	@Bean
	RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		JdbcRegisteredClientRepository jdbcRegisteredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);
		RegisteredClient client1 = jdbcRegisteredClientRepository.findByClientId("client1");
		if (client1 == null) {
			RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("client1").clientSecret(passwordEncoder().encode("1234")).clientSettings(clientSettings())
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.redirectUri("https://oidcdebugger.com/debug")
				.redirectUri("http://jcornor.com:8000/callback")
				.redirectUri("https://oauth.pstmn.io/v1/callback")
				.scope(OidcScopes.OPENID).scope("profiles").scope("contacts").scope("messages")
				.tokenSettings(tokenSettings()).build();
			jdbcRegisteredClientRepository.save(registeredClient);
		}
		JdbcRegisteredClientRepository.RegisteredClientParametersMapper registeredClientParametersMapper = new JdbcRegisteredClientRepository.RegisteredClientParametersMapper();
		jdbcRegisteredClientRepository.setRegisteredClientParametersMapper(registeredClientParametersMapper);

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
