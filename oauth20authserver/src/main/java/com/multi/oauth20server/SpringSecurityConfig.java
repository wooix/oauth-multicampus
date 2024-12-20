package com.multi.oauth20server;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	private final DataSource datasource;

	public SpringSecurityConfig(DataSource datasource) {
		this.datasource = datasource;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// 캬.. 이거 잘봐라.. 생각의 틀을 좀 깨자...
	@Bean
	UserDetailsService user() {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(datasource);
		// @formatter:off
		   try {
		     jdbcUserDetailsManager.loadUserByUsername("user1");
		   } catch (UsernameNotFoundException ex) {
		     PasswordEncoder encoder = new BCryptPasswordEncoder();
		     UserDetails user =
		         User.withUsername("user1")
		           .passwordEncoder(encoder::encode).password("1234")
		           .roles("USER").build();
		     jdbcUserDetailsManager.createUser(user);
		   }
		   // @formatter:on
		return jdbcUserDetailsManager;
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.formLogin(Customizer.withDefaults());
		return http.build();
	}
}
