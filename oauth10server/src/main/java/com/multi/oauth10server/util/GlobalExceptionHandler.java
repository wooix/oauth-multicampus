package com.multi.oauth10server.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.thinker.oauth.util.OAuthException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value={ OAuthException.class })
	@ResponseBody
	public ResponseEntity<OAuthErrorInfo> handleApiKeyException(OAuthException e) {
		OAuthErrorInfo error = new OAuthErrorInfo(e.getMessage(), e.getStatus());
		ResponseEntity<OAuthErrorInfo> entity = new ResponseEntity<OAuthErrorInfo>(error, HttpStatus.UNAUTHORIZED);
		return entity;
	}
	
	@ExceptionHandler(value={ Exception.class })
	@ResponseBody
	public ResponseEntity<OAuthErrorInfo> handleCustomException(Exception e) {
		OAuthErrorInfo error = new OAuthErrorInfo(e.getMessage(), "E100");
		ResponseEntity<OAuthErrorInfo> entity = new ResponseEntity<OAuthErrorInfo>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		return entity;
	}
}
