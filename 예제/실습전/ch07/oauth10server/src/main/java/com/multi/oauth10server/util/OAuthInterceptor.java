package com.multi.oauth10server.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.thinker.oauth.parameter.OAuthTokenParam;

import com.multi.oauth10server.model.ConsumerVO;
import com.multi.oauth10server.model.UsersVO;
import com.multi.oauth10server.service.ConsumerService;
import com.multi.oauth10server.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthInterceptor  implements HandlerInterceptor {
	@Autowired
	UsersService usersService;
	@Autowired
	ConsumerService consumerService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//이곳에 코드를 작성합니다. 
        
        return true;
	}
}
