package com.multi.contactsapp.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Contacdt 객체 한건을 의미함")
@JacksonXmlRootElement(localName = "contact")
public class Contact {
	@JacksonXmlProperty(isAttribute = true)
	@Schema(title="일련번호", example="1101")
	@NotBlank
	@Size(min=0, max = 999999)
	private long no;
		
	@NotNull
	@Schema(title="이름", example="이상호")
	private String name;
	
//	@JsonProperty(value="phone")
	@NotNull
	@Schema(title = "전화번호", example="010-1234-5678")
	private String tel;
	
	@Schema(title = "주소", example="서울시")
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
