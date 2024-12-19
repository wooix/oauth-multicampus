package com.multi.contactsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.domain.Contact;
import com.multi.contactsapp.domain.ContactList;
import com.multi.contactsapp.domain.Result;
import com.multi.contactsapp.service.ContactService;

@RestController
@RequestMapping(value = "/contacts")
public class ContactRestController {
	@Autowired
	private ContactService contactService;

	@GetMapping()
	public ContactList getContactList(@RequestParam(value = "pageno", required = false, defaultValue = "0") int pageNo,
			@RequestParam(value = "pagesize", required = false, defaultValue = "3") int pageSize) {
		ContactList contactList = null;
		if (pageNo < 1) {
			contactList = contactService.getContactList();
		} else {
			if (pageSize < 2)
				pageSize = 5;
			contactList = contactService.getContactList(pageNo, pageSize);
		}
		return contactList;
	}

	@GetMapping("{no}")
	public Contact getContactOne(@PathVariable("no") int no) {
		Contact c = new Contact();
		c.setNo(no);
		return contactService.getContactOne(c);
	}

	@PostMapping()
	public Result insertContact(@RequestBody Contact c) {
		return contactService.insertContact(c);
	}

	@PutMapping("{no}")
	public Result updateContact(@PathVariable("no") int no, @RequestBody Contact c) {
		c.setNo(no);
		return contactService.updateContact(c);
	}

	@DeleteMapping("{no}")
	public Result updateContact(@PathVariable("no") int no) {
		Contact c = new Contact();
		c.setNo(no);
		return contactService.deleteContact(c);
	}

}
