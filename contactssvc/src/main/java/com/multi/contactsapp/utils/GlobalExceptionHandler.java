package com.multi.contactsapp.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler({ ApiException.class })
	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiErrorInfo> handleCustomException(ApiException e) {
		ApiErrorInfo error = new ApiErrorInfo("@GlobalExceptionHandler : " + e.getMessage(), e.getStatus());
		ResponseEntity<ApiErrorInfo> entity = new ResponseEntity<ApiErrorInfo>(error, HttpStatus.BAD_REQUEST);
		return entity;
	}
}
