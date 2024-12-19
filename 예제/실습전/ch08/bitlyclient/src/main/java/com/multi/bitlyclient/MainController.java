package com.multi.bitlyclient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/main")
public class MainController {
	@GetMapping()
	public String requestMain(HttpSession session) {
		if (session.getAttribute("access_token") == null) 
			return "redirect:/";
		return "main";
	}
}
