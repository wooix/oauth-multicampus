package com.multi.contactsapp.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "result")
public class Result {
	private String status;
	private String message;
	private String key;

	public Result(String status, String message, String key) {
		super();
		this.status = status;
		this.message = message;
		this.key = key;
	}

	public Result() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", message=" + message + ", key=" + key + "]";
	}

}
