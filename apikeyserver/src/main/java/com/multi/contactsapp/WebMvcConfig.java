package com.multi.contactsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.multi.contactsapp.openapi.ApiKeyInterceptor;

@Component
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	ApiKeyInterceptor apiKeyInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(apiKeyInterceptor)
		.addPathPatterns("/contacts/**");
	}

}
