package com.multi.contactsapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.domain.Contact;
import com.multi.contactsapp.util.ApiErrorInfo;
import com.multi.contactsapp.util.ApiException;

@RestController
@RequestMapping(value = "/test")
public class TestController {

	@GetMapping("error1")
	public Contact getTestContactOne(@RequestParam(value = "name", required = false) String name) {
		if (name == null || name.equals(""))
			throw new ApiException("name 파라미터는 비워둘 수 없습니다.");
		else
			return new Contact(0, name, "", "");
	}

//	@ExceptionHandler(value = { ApiException.class })
//	public ResponseEntity<ApiErrorInfo> handleCustomException(ApiException e) {
//		ApiErrorInfo error = new ApiErrorInfo("@ExceptionHandler : " + e.getMessage(), e.getStatus());
//		ResponseEntity<ApiErrorInfo> entity = new ResponseEntity<ApiErrorInfo>(error, HttpStatus.BAD_REQUEST);
//		return entity;
//	}
}
