package com.multi.contactsapp.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "contact")
public class Contact {
	@JacksonXmlProperty(isAttribute = true)
	private long no;
	private String name;
//	@JsonProperty(value="phone")
	private String tel;
	private String address;

	// parameter있는 생성자를 만들었다면 기본 생성자를 만들어라. !!!
	// 직렬화할때 오류가 발생한다. 
	public Contact() {
		super();
	}

	public Contact(long no, String name, String tel, String address) {
		this.no = no;
		this.name = name;
		this.tel = tel;
		this.address = address;
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

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
