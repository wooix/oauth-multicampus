package com.multi.oauth20server;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.oauth20server.dao.ContactRepository;
import com.multi.oauth20server.domain.Contact;

@RestController
@RequestMapping(value = "/api")
public class ContactController {
	@Autowired
	ContactRepository contactRepository;

	@GetMapping(value = "/contacts", produces = { "application/json" })
	public List<Contact> getContactList() {
		return contactRepository.findAll();
	}

	@GetMapping(value = "/profiles", produces = { "application/json" })
	public HashMap<String, String> getProfile() {
		HashMap<String, String> profile = new HashMap<String, String>();
		profile.put("system", "OAuth 2.0 Resource Server");
		profile.put("devenv", "Spring Boot 3.x");
		return profile;
	}
}
