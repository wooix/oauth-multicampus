package com.multi.contactsapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.multi.contactsapp.mapper")
@SpringBootApplication
public class ContactswebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactswebApplication.class, args);
	}

}
