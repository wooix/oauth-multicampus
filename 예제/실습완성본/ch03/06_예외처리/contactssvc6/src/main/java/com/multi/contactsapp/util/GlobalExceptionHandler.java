package com.multi.contactsapp.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value={ ApiException.class })
  @ResponseBody
  public ResponseEntity<ApiErrorInfo> handleCustomException(ApiException e) {
    ApiErrorInfo error = new ApiErrorInfo("@ControllerAdvice : " + e.getMessage(), e.getStatus());
    ResponseEntity<ApiErrorInfo> entity = 
          new ResponseEntity<ApiErrorInfo>(error, HttpStatus.BAD_REQUEST);
    return entity;
  }
}

