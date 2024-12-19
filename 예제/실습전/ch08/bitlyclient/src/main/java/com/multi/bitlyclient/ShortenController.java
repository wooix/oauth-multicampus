package com.multi.bitlyclient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/shorten")
public class ShortenController {
	
	@GetMapping()
	public void requestMain(HttpSession session, HttpServletResponse res, 
			@RequestParam(value = "long_url") String long_url) throws Exception {
		//이곳에 코드를 작성합니다.		

	}
}
