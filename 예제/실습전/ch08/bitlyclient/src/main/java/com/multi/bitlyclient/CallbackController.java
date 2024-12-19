package com.multi.bitlyclient;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/callback")
public class CallbackController {
	
	@GetMapping()
	public String requestCallback(HttpSession session, Model model, 
			@RequestParam(value = "code") String code)
			throws ClientProtocolException, IOException {
		//이곳에 코드를 작성합니다. 에러 페이지도 함께 검토합니다.(src/main/resources/templates/error.html)
		
		//아래코드는 삭제하고 작성하세요.
		return null;
	}
}
