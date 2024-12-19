package com.multi.contactsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.multi.contactsapp.openapi.ApiKeyProcessor;
import com.multi.contactsapp.openapi.ApiKeyVO;

@Controller
public class KeyProcessController {
	
	@Autowired
	private ApiKeyProcessor keyProcessor;
	
	@GetMapping("/apikey/request")
	public void getForm(){		

	}
	
	@PostMapping("/apikey/makekey")
	public void makeApiKey(ApiKeyVO apiKeyVO, Model model)throws Exception{

		String keyValue = keyProcessor.requestNewAPIKey(apiKeyVO);
		model.addAttribute("apikey", keyValue);
	
	}
}
