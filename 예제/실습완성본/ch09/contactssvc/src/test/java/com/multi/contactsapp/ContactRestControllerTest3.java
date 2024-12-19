package com.multi.contactsapp;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.contactsapp.domain.Contact;
import com.multi.contactsapp.domain.ContactList;
import com.multi.contactsapp.domain.Result;
import com.multi.contactsapp.util.ApiErrorInfo;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class ContactRestControllerTest3 {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void getAllContactsReturnsOkWithListOfContacts_3() throws Exception {
		ResponseEntity<ContactList> entity = 
			restTemplate.getForEntity("http://localhost:8080/contacts?pageno=1&pagesize=4", ContactList.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode()); 

		ContactList contactList = entity.getBody();
		assertEquals(1, contactList.getPageNo());
		assertEquals(4, contactList.getPageSize());
		assertEquals(4, contactList.getContacts().size());
	}

	@Test
	public void insertContactReturnsOkWithValidStatus_3() throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		headers.setAccept(mediaTypes);

		HttpEntity<Contact> reqEntity = new HttpEntity<Contact>(
			new Contact(0, "다희2", "010-5555-7777", "제주"), headers);
		
		ResponseEntity<Result> entity = restTemplate.postForEntity(
			"http://localhost:8080/contacts", reqEntity, Result.class);
		assertEquals(HttpStatus.OK, entity.getStatusCode()); 
		Result result = entity.getBody();
		assertEquals("ok", result.getStatus());
	}
	
	@Test
	public void insertInvalidContactReturnsOkWithBadRequest_3() throws JsonMappingException, JsonProcessingException {
		HttpStatusCode status;
		String apiStatus;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			List<MediaType> mediaTypes = new ArrayList<MediaType>();
			headers.setAccept(mediaTypes);
	
			HttpEntity<Contact> reqEntity = new HttpEntity<Contact>(
				new Contact(0, "", "010-5555-7777", ""), headers);
			ResponseEntity<ApiErrorInfo> entity = restTemplate.postForEntity("http://localhost:8080/contacts", reqEntity, ApiErrorInfo.class);
			status= entity.getStatusCode();
			apiStatus = entity.getBody().getStatus();
			System.out.println(status);
		} catch (HttpStatusCodeException  e) {
			status = e.getStatusCode();
			ApiErrorInfo apiErrorInfo = objectMapper.readValue(e.getResponseBodyAsString(), ApiErrorInfo.class);
			apiStatus = apiErrorInfo.getStatus();
			System.out.println(status);
		}
		assertEquals(HttpStatus.BAD_REQUEST, status); 
		assertEquals("102", apiStatus);
	}
	
	@Test
	public void invalidRequestPathReturns404_3() throws Exception {
		HttpStatusCode status;
		try {
			ResponseEntity<Object> entity = restTemplate.getForEntity("http://localhost:8080/asdf", Object.class);
			status = entity.getStatusCode();
		} catch (HttpStatusCodeException  e) {
			status = e.getStatusCode();
		}
		
		assertEquals(HttpStatus.NOT_FOUND, status); 
	}
	
}
