package com.multi.contactsapp.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

	@Autowired()
	private ApiKeyProcessor keyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String origin = (String) request.getHeader("Origin");
		String apiKey = request.getParameter("key");
		keyService.checkApiKey(origin, apiKey);
		return true;
	}
}