package com.multi.contactsapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multi.contactsapp.domain.Contact;
import com.multi.contactsapp.service.ContactService;

@Controller
@RequestMapping(value="/contacts")
public class ContactController {
	@Autowired
	private ContactService contactService;
	
	@GetMapping()
	public void getContactList(Model model) {
		List<Contact> contacts = contactService.getContactList();
		model.addAttribute("contacts", contacts);
	}
	
	@PostMapping("add")
	public String  insertContact(Contact c) {
		contactService.insertContact(c);
		return "redirect:/contacts";
	}
	
	@PostMapping("update")
	public String  updateContact(Contact c) {
		contactService.updateContact(c);
		return "redirect:/contacts";
	}
}

