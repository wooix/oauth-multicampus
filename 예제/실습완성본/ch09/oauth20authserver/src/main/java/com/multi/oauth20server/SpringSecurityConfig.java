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

	// application.properies의 Data Source 설정을 사용하기 위함 멤버필드
	private final DataSource dataSource;

	// 생성자를 이용해 Data Source 의존성 주입
	public SpringSecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	// 사용자 password를 일방향 암호화하기 위한 Bean 설정
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * * oauth 인증과정중 사용자 로그인을 위한 jdbc를 이용 사용자 서비스 - data source를 사용하는
	 * jdbcUserDetailsMananer Bean을 리턴하면 됨 - try~catch 문의 코드는 다른 Bean을 이용해 사용자를 등록할
	 * 때 사용하면 됨
	 */
	@Bean
	UserDetailsService users() {
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
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

//  만일 InMemoryUserDetailsManager 를 이용하고 싶다면 아래 코드를 사용함
//  @Bean
//  UserDetailsService users() {
//    UserDetails user1 = 
//         User.withDefaultPasswordEncoder().username("user1").password("1234").roles("USERS").build();
//    UserDetails user2 =  
//         User.withDefaultPasswordEncoder().username("user2").password("1234").roles("USERS").build();
//    UserDetails admin = 
//        User.withDefaultPasswordEncoder().username("admin").password("1234").roles("ADMINS").build();
//
//    return new InMemoryUserDetailsManager(user1, user2, admin);
//  }

	// 1. 사용자 인증을 위한 UserDetailsService를 이용하도록 설정
	// 2. Password 인코더로 BCryptPasswordEncoder 사용
	@Bean
	DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(userDetailsService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}

	/**
	 * * 보안 설정 - AuthorizationServerConfiguration에서 기본 보안설정을 적용할 것이므로 여기서는 모든 요청에 대해
	 * 인증을 요구하는 것으로 설정해도 됨 - FormLogin을 Default로 설정
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
        http
            .authorizeHttpRequests(authorize ->
              authorize.anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults());
        return http.build();
	  	// @formatter:on
	}

}
