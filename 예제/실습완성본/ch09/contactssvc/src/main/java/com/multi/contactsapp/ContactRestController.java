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
import com.multi.contactsapp.util.ApiException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/contacts")
@Tag(name = "연락처 API", description = "연락처 서비스 API 상세 설명")
public class ContactRestController {
	@Autowired
	private ContactService contactService;

//@formatter:off
	@Operation(
			summary = "연락처 목록 조회", 
			description = "페이징을 사용해 연락처 목록 조회", 
			responses = {
					@ApiResponse(responseCode = "200", description = "조회된 연락처 정보를 반환함."),
					@ApiResponse(responseCode = "400", description = "API Exception 오류 발생")
			}
	)
	@GetMapping()
//@formatter:on
	public ContactList getContactList(
			@Parameter(description = "페이지 번호", example = "2")
				@RequestParam(value = "pageno", required = false, defaultValue = "0") int pageNo,
			@Parameter(description = "페이지 사이즈", example = "3")
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

//@formatter:off
	@Operation(
			summary = "연락처 추가", 
			description = "JSON/XML로 전달된 연락처 한건 정보를 추가함", 
			responses = {
					@ApiResponse(responseCode = "200", description = "추가 성공/실패 여부 응답, Result객체"),
					@ApiResponse(responseCode = "400", description = "API Exception 오류 발생")
			}
	)
	@PostMapping()
//@formatter:on
	public Result insertContact(
			 @Valid @RequestBody Contact c) {

		String name = c.getName();
		String tel = c.getTel();
		if (name == null || name.equals("") || tel == null || tel.equals("")) {
			throw new ApiException("이름과 전화번호는 필수 입력 항목입니다.", "102");

		}
		return contactService.insertContact(c);
	}

	@PutMapping("{no}")
	public Result updateContact(@PathVariable("no") int no, @RequestBody Contact c) {
		String name = c.getName();
		String tel = c.getTel();
		if (name == null || name.equals("") || tel == null || tel.equals("")) {
			throw new ApiException("이름과 전화번호는 필수 입력 항목입니다.", "102");
		}
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
