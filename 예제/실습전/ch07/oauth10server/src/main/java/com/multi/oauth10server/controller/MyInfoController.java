package com.multi.oauth10server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.oauth10server.model.ConsumerVO;
import com.multi.oauth10server.model.UsersVO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class MyInfoController {

	@GetMapping(value="/api/myinfo")
	public UsersVO getMyInfo(HttpServletRequest request) throws Exception {
		UsersVO usersVO = (UsersVO)request.getAttribute("usersVO");
		ConsumerVO consumerVO = (ConsumerVO)request.getAttribute("consumerVO");
		System.out.println(consumerVO);
		return usersVO;
	}
	
}
