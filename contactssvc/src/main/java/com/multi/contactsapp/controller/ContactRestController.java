package com.multi.contactsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.domain.Contact;
import com.multi.contactsapp.domain.ContactList;
import com.multi.contactsapp.domain.Result;
import com.multi.contactsapp.service.ContactService;
import com.multi.contactsapp.utils.ApiException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/contacts")
//@CrossOrigin(origins = { "http://client:8000", "http://jcornor.com:8000" })
//@CrossOrigin(originPatterns = )
@Tag(name = "연락처 API Controller", description = "연락처 API 상세 설명", externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(description = "연락처 API 상세 설명", url = "https://www.naver.com"))
public class ContactRestController {
	@Autowired
	ContactService contactService;

	@Operation(
		summary = "연락처 목록 조회",
		description = "페이징을 통해 연락처 데이터 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "조회된 연락처 데이터를 응답합니다."),
			@ApiResponse(responseCode = "400", description = "잘못된 요청, ApiException 오류발생.")
		},
		parameters = {
			@Parameter(name="pageno", description="페이지번호", example="1"),
			@Parameter(name="pagesize", description="페이지단원", example="2")
		}
	)	
	@GetMapping
	public ContactList getContactList(@RequestParam(value = "pageno", required = false, defaultValue = "1") int pageNo,
			@RequestParam(value = "pagesize", required = false, defaultValue = "2") int pageSize) {
		ContactList contactList = null;
		if (pageNo < 1) {
			contactList = contactService.getContactList();
		} else {
			if (pageSize < 2)
				pageSize = 2;
			if (pageSize > 20)
				pageSize = 20;
			contactList = contactService.getContactList(pageNo, pageSize);
		}
		return contactList;
	}

	@GetMapping("{no}")
	public Contact getContactOne(@PathVariable int no) {
		Contact c = new Contact();
		c.setNo(no);
		return contactService.getContactOne(c);
	}

	@Operation(
			summary = "연락처 추가",
			description = "연락처 추가 작업을 합니다.",
			responses = {
				@ApiResponse(responseCode = "200", description = "조회된 연락처 데이터를 응답합니다."),
				@ApiResponse(responseCode = "400", description = "잘못된 요청, ApiException 오류발생.")
			},
			requestBody = @RequestBody(content = {
					@Content(propertyNames = @Schema(name = "contact"))
			})
	)
	@PostMapping()
	public Result insertContact(@Valid @RequestBody Contact c) {
		String name = c.getName();
		String tel = c.getTel();
		if (name == null || name.equals("") || tel == null || tel.equals("")) {
			throw new ApiException("이름과 전화번호는 필수 입력 항목입니다.", "101");
		}
		return contactService.insertContact(c);
	}

	@DeleteMapping("{no}")
	public Result deleteContact(@PathVariable int no) {
		Contact c = new Contact();
		c.setNo(no);
		return contactService.deleteContact(c);
	}

	@PutMapping("{no}")
	public Result updateContact(@PathVariable int no, @RequestBody Contact c) {
		String name = c.getName();
		String tel = c.getTel();
		if (name == null || name.equals("") || tel == null || tel.equals("")) {
			throw new ApiException("이름과 전화번호는 필수 입력 항목입니다.", "102");
		}
		c.setNo(no);
		return contactService.updateContact(c);
	}
}
