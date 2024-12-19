package com.multi.contactsapp;

import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.multi.contactsapp.service.ContactService;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactRestControllerTest2 {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ContactService contactService;

	@Test
	public void getAllContactsReturnsOkWithListOfContacts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/contacts").param("pageno", "1").param("pagesize", "4")
					.accept(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageNo", Matchers.is(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.pageSize", Matchers.is(4)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.contacts", Matchers.hasSize(4)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.contacts[0].no", Matchers.greaterThan(0)))
			.andExpect(MockMvcResultMatchers.jsonPath("$.contacts[0].name", Matchers.matchesRegex(Pattern.compile("[가-힣]+"))))
			.andExpect(MockMvcResultMatchers.jsonPath("$.contacts[0].tel", Matchers.matchesRegex(Pattern.compile("^01[0-9]-\\d{3,4}-\\d{4}$"))));
		
	}

	@Test
	public void insertContactReturnsOkWithValidStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/contacts").contentType(MediaType.APPLICATION_JSON)
				.content("{ \"name\" : \"다희\", \"tel\" : \"010-9999-9999\", \"address\" : \"서울\" }")
				.accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ok"))
			.andDo(MockMvcResultHandlers.print());
	}

	@Test
	public void invalidRequestPathReturns404() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/asdf").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void invalidRequestReturns400WithAppStatus() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test/error1").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value("100"));
	}
}
