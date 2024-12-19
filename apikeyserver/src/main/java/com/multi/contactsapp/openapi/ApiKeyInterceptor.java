package com.multi.contactsapp.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

	@Autowired()
	private ApiKeyProcessor keyService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String origin = request.getHeader("Origin");
		String apiKey = request.getParameter("key");
		System.out.println(apiKey);
		keyService.checkApiKey(origin, apiKey);
		return true;
	}

}