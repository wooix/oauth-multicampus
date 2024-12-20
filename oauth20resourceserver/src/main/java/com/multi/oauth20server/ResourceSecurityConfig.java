package com.multi.oauth20server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceSecurityConfig {
	
	//권한 검증 하지 않을 경로 지정!!!!
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/webjars/**","/assets/**");
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(Customizer.withDefaults())
		.authorizeHttpRequests((authz) -> authz
				.requestMatchers("/swagger/**").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll()
				.requestMatchers("/swagger-ui.html").permitAll()
				.requestMatchers("/v3/api-docs/**").permitAll()
				.requestMatchers("/api/contacts").hasAuthority("SCOPE_contacts")
				.requestMatchers("/api/profiles").hasAuthority("SCOPE_profiles")
				.anyRequest().authenticated()
				)
		.oauth2ResourceServer( oauth2 -> oauth2.jwt(Customizer.withDefaults())				);
		return http.build();		
	}
}
