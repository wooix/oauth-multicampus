package com.multi.oauth10server.util;

public class OAuthErrorInfo {
	private String message;
	private String status;
	
	public OAuthErrorInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OAuthErrorInfo(String message, String status) {
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
