package com.multi.contactsapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.multi.contactsapp.domain.Contact;

@Mapper
public interface ContactMapper {
	 List<Contact> getContactList();

	 List<Contact> getContactList(int pageNo, int pageSize);

	 long insertContact(Contact contact);

	 int getContactCount();

	 Contact getContactOne(Contact c);

	 int updateContact(Contact c);

	 int deleteContact(Contact c);

}
