package com.multi.contactsapp;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

//@formatter:off
@Configuration
@OpenAPIDefinition(
		info = @Info(
				title = "연락처 서비스 문서화", 
				description = "연락처 서비스 API에 대한 설명", 
				version = "1.0"
		)
)
public class SpringDocConfig {
}
//@formatter:on