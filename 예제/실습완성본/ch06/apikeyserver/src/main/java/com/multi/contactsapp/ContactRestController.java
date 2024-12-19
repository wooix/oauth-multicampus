package com.multi.contactsapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.domain.ContactList;
import com.multi.contactsapp.service.ContactService;

@CrossOrigin(origins="*")
@RestController
@RequestMapping(value="contacts")
public class ContactRestController {
  @Autowired
  private ContactService contactService;
 
  @GetMapping()
  public ContactList getContactList() {
    return contactService.getContactList();
  }
}

