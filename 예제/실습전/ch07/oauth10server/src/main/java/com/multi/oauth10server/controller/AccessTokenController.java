package com.multi.oauth10server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.multi.oauth10server.service.ConsumerService;
import com.multi.oauth10server.service.RequestTokenService;
import com.multi.oauth10server.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/oauth/access_token")
public class AccessTokenController {

	@Autowired
	private RequestTokenService requestTokenService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private ConsumerService consumerService;

	@GetMapping
	public ModelAndView getAccessToken(HttpServletRequest request) throws Exception {
		//이곳에 코드를 작성합니다. 다음 코드는 삭제하고 작성합니다.
		return null;
	}
}
