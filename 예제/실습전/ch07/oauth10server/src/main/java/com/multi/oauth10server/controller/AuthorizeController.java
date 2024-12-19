package com.multi.oauth10server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.multi.oauth10server.service.RequestTokenService;
import com.multi.oauth10server.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/oauth/authorize")
public class AuthorizeController {

	@Autowired
	private RequestTokenService requestTokenService;

	@Autowired
	private UsersService usersService;

	@GetMapping
	public ModelAndView authorizeGet(HttpServletRequest request) throws Exception {
		//이곳에 코드를 작성합니다. 다음 코드는 삭제하고 작성합니다.
		return null;
	}

	@PostMapping
	public ModelAndView authorizePost(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//이곳에 코드를 작성합니다. 다음 코드는 삭제하고 작성합니다.
		return null;
	}

}
