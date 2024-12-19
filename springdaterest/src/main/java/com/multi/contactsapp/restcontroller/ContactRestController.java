package com.multi.contactsapp.restcontroller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.dao.ContactRepository;
import com.multi.contactsapp.domain.Contact;

@RestController
@RequestMapping("/contacts")
public class ContactRestController {
	@Autowired
	ContactRepository contactRepository;

	@GetMapping
	public Page<Contact> getContactAll(Pageable pageable) {
		return contactRepository.findAll(pageable);
	}

	@GetMapping("{no}")
	public Optional<Contact> getContactOne(@PathVariable("no") long no) {
		return contactRepository.findById(no);
	}

	@PostMapping
	public Contact insertContact(@RequestBody Contact c) {
		return contactRepository.save(c);
	}

	@PutMapping("{no}")
	public Contact updateContact(@RequestBody Contact c, @PathVariable long no) {
		c.setNo(no);
		return contactRepository.save(c);
	}

	@DeleteMapping("{no}")
	public void deleteContact(@PathVariable long no) {
		contactRepository.deleteById(no);
	}

}
