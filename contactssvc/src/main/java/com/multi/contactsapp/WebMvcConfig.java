package com.multi.contactsapp;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	// ....?output=xml 헐헐헐...
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//		WebMvcConfigurer.super.configureContentNegotiation(configurer);
		configurer.defaultContentType(MediaType.APPLICATION_JSON).favorParameter(true).parameterName("output")
				.ignoreAcceptHeader(false);
//		.mediaType("xml", MediaType.APPLICATION_XML)
//		.mediaType("json", MediaType.APPLICATION_JSON);

	}

	// 정적자원 추가를 위한 설정
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
//		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/photos/**").addResourceLocations("file:///c:/dev/photos/")
				.setCacheControl(CacheControl.maxAge(Duration.ofDays(1)));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/contacts/**")
			.allowedOrigins("http://client:8000", "http://jcornor:8000")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowCredentials(true)
			.maxAge(3600);
	}

	// 혹은
	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() {
	 * CorsConfiguration config = new CorsConfiguration();
	 * config.addAllowedOrigin("https://example.com"); config.addAllowedMethod("*");
	 * config.addAllowedHeader("*");
	 * 
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * config); return source; }
	 */

}
