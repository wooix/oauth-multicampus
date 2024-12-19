package com.multi.oauth20server;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
		info = @Info(
				title = "OAuth2 리소스 서버 문서화 테스트",	
				description = "OAuth2 리소스 서버에 대한 상세 설명", 
				version = "1.0"
		)
)
@SecurityScheme(
		name = "oauth2_auth", 
		type = SecuritySchemeType.OAUTH2,
		flows = @OAuthFlows(
				authorizationCode = @OAuthFlow(
						authorizationUrl = "http://tfactory.com/oauth2/authorize", 
						tokenUrl = "http://tfactory.com/oauth2/token", 
						scopes = {
								@OAuthScope(name = "messages", description = "message scope"),
								@OAuthScope(name = "contacts", description = "contacts scope"),
								@OAuthScope(name = "profiles", description = "profiles scope") 
						}
				)
		)
)
public class SpringDocConfig {

}
