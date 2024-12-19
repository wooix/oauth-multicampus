package com.multi.oauth20server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class ResourceSecurityConfig {

	// 권한 검증 하지 않을 경로 지정
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/webjars/**", "/assets/**");
	}

	/**
	 * * 권한 적용 
	 *   - Swagger 관련 경로는 모두 허용 
	 *   - /api/contacts : contacts SCOPE가 있어야 함 
	 *   - /api/profiles : profiles SCOPE가 있어야 함 
	 * * OAuth2 Resource Server에 JWT 적용
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
	    http.csrf(Customizer.withDefaults())
	        .authorizeHttpRequests((authz) -> authz
	          .requestMatchers("/swagger/**").permitAll()
	          .requestMatchers("/swagger-ui/**").permitAll()
	          .requestMatchers("/api/contacts").hasAuthority("SCOPE_contacts")
	          .requestMatchers("/api/profiles").hasAuthority("SCOPE_profiles")
	            .anyRequest().authenticated()
	        )
	        .oauth2ResourceServer(oauth2 -> oauth2
	            .jwt(Customizer.withDefaults())
	        );
	    // @formatter:on
		return http.build();
	}
}
