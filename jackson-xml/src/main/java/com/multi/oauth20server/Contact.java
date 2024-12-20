package com.multi.oauth20server;

public class Contact {
	private long no;
	private String name;
	public Contact() {
		super();
		this.no = 100;
		this.name = "hello";
		// TODO Auto-generated constructor stub
	}
	public Contact(long no, String name) {
		super();
		this.no = no;
		this.name = name;
	}
	public long getNo() {
		return no;
	}
	public void setNo(long no) {
		this.no = no;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
