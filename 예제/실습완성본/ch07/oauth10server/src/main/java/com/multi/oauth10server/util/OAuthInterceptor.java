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
		
        OAuthTokenParam param = new OAuthTokenParam(request);
		
        long userNo = param.getUserNo();
        String consumerKey = param.getConsumerKey();
        UsersVO usersVO = usersService.selectUserByUserNo(userNo);
        ConsumerVO consumerVO = consumerService.selectByConsumerKey(consumerKey);	
        
        param.validateRequestToken(consumerVO, usersVO);
		
        request.setAttribute("usersVO", usersVO);
        request.setAttribute("consumerVO", consumerVO);
        
        return true;
	}
}
