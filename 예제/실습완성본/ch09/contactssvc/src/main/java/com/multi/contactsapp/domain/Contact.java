package com.multi.contactsapp.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(title = "연락처 한건 정보")
@JacksonXmlRootElement(localName = "contact")
public class Contact {
	@NotBlank
	@Size(min = 0)
	@Schema(title = "일련번호", example = "1001")
	@JacksonXmlProperty(isAttribute = true)
	private long no;
	@NotNull
	@Schema(title = "이름", example = "홍길동")
	private String name;
	@NotBlank
	@Schema(title = "전화번호", example = "010-1111-2222")
	@JacksonXmlProperty(localName = "phone")
	private String tel;
	@Schema(title = "주소", example = "서울시")
	private String address;

	// 생성자, Setter, Getter 자동 생성
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(long no, String name, String tel, String address) {
		super();
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

	@Override
	public String toString() {
		return "Contact [no=" + no + ", name=" + name + ", tel=" + tel + ", address=" + address + "]";
	}

}
