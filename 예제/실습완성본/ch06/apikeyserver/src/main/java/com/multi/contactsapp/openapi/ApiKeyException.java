package com.multi.contactsapp.openapi;

public class ApiKeyException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String status;
	
	public ApiKeyException(String message, String status) {
		super(message);
		this.status = status;
	}
	
	public ApiKeyException(String message) {
		this(message, "100");
	}
	
	public String getStatus(){
		return this.status;
	}
}