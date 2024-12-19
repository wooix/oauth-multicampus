package com.multi.oauth10server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.multi.oauth10server.util.OAuthInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	OAuthInterceptor inteceptor; 
	
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(inteceptor)
                .addPathPatterns("/api/**");
    }
}