package com.multi.contactsapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.multi.contactsapp.domain.Contact;
import com.multi.contactsapp.utils.ApiErrorInfo;
import com.multi.contactsapp.utils.ApiException;

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
	
//	@ExceptionHandler(value = {ApiException.class})
	//ResponseEntity를 이용하는 이유는 @RestController의 ResponseEntity를 그냥 이용하면 http 200으로 처리되는것을 
	// 방지하기 위해서 직접 만든 ResponseEntity로 반환하는 거얌.
//	public ResponseEntity<ApiErrorInfo> handleCustomException(ApiException e) {
//		ApiErrorInfo error = new ApiErrorInfo("@ExceptionHandler : " + e.getMessage(), e.getStatus());
//		ResponseEntity<ApiErrorInfo> entity = 
//				new ResponseEntity<ApiErrorInfo>(error, HttpStatus.BAD_REQUEST);
//		return entity;
//	}
}
