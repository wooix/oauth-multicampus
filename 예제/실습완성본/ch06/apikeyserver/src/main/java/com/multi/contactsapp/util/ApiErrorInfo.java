package com.multi.contactsapp.util;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="restApiError")
public class ApiErrorInfo {
	private String message;
	private String status;
	
	public ApiErrorInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ApiErrorInfo(String message, String status) {
		super();
		this.message = message;
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ApiErrorInfo [message=" + message + ", status=" + status + "]";
	}
	
	
}
