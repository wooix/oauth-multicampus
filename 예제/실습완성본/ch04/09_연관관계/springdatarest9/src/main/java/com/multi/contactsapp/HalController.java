package com.multi.contactsapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.dao.ContactRepository;
import com.multi.contactsapp.domain.Contact;

@RequestMapping(value = "/contacts3")
@RestController
public class HalController {
	@Autowired
	private ContactRepository contactRepository;

	@GetMapping("{no}")
	public ResponseEntity<EntityModel<Contact>> getContactByNo(@PathVariable("no") long no) {
		Contact contact = contactRepository.findById(no).get();
		EntityModel<Contact> entityModel = EntityModel.of(contact);
		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getContactByNo(no));
		entityModel.add(linkTo.withSelfRel());
		return ResponseEntity.ok(entityModel);
	}

	@GetMapping()
	public ResponseEntity<CollectionModel<EntityModel<Contact>>> getContactsAll() {
		List<EntityModel<Contact>> result = new ArrayList<>();
		List<Contact> contacts = contactRepository.findAll();
		for (final Contact contact : contacts) {
			EntityModel<Contact> entityModel = EntityModel.of(contact);
			Link selfLink = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getContactByNo(contact.getNo())).withSelfRel();
			result.add(entityModel.add(selfLink));
		}
		Link link = WebMvcLinkBuilder.linkTo(this.getClass()).withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(result,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getContactsAll()).withSelfRel()));
	}

}
