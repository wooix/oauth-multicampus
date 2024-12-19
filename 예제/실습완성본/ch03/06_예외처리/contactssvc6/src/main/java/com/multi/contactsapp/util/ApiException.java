package com.multi.contactsapp.util;

public class ApiException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  private final String status;
  
  public ApiException(String message, String status) {
    super(message);
    this.status = status;
  }
  
  public ApiException(String message) {
    this(message, "100");
  }
  
  public String getStatus(){
    return this.status;
  }
}
