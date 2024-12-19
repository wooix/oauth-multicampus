package com.multi.bitlyclient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {

	@GetMapping
	public String requestIndex(Model model) {
		//이곳에 코드를 작성합니다. src/main/resources/templates/index.html 도 함께 검토하세요

	    return "index";

	}
}
