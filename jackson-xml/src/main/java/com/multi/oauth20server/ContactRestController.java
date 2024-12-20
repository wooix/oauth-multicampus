package com.multi.oauth20server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contacts")
public class ContactRestController {

	@GetMapping
	public Contact getContactAll() {
		return new Contact();
	}

	
}
