package com.multi.contactsapp.openapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

	@Autowired()
	private ApiKeyProcessor keyService;

	//이곳에 preHandle 메서드를 재정의한 후 코드를 작성합니다.

}