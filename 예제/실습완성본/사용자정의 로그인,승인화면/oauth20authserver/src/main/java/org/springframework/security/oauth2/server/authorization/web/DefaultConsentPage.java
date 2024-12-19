package org.springframework.security.oauth2.server.authorization.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;

//https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/web/DefaultConsentPage.java
class DefaultConsentPage {
	private static final MediaType TEXT_HTML_UTF8 = new MediaType("text", "html", StandardCharsets.UTF_8);

	private DefaultConsentPage() {
	}

	static void displayConsent(HttpServletRequest request, HttpServletResponse response, String clientId,
			Authentication principal, Set<String> requestedScopes, Set<String> authorizedScopes, String state,
			Map<String, String> additionalParameters) throws IOException {

		String consentPage = generateConsentPage(request, clientId, principal, requestedScopes, authorizedScopes, state, additionalParameters);
		response.setContentType(TEXT_HTML_UTF8.toString());
		response.setContentLength(consentPage.getBytes(StandardCharsets.UTF_8).length);
		response.getWriter().write(consentPage);
	}

	private static String generateConsentPage(HttpServletRequest request,
			String clientId, Authentication principal, Set<String> requestedScopes, Set<String> authorizedScopes, String state,
			Map<String, String> additionalParameters) {
		Set<String> scopesToAuthorize = new HashSet<>();
		Set<String> scopesPreviouslyAuthorized = new HashSet<>();
		for (String scope : requestedScopes) {
			if (authorizedScopes.contains(scope)) {
				scopesPreviouslyAuthorized.add(scope);
			} else if (!scope.equals(OidcScopes.OPENID)) { 
				scopesToAuthorize.add(scope);
			}
		}

		String userCode = additionalParameters.get(OAuth2ParameterNames.USER_CODE);

		StringBuilder builder = new StringBuilder();

		builder.append("<!DOCTYPE html>");
		builder.append("<html lang=\"en\">");
		builder.append("<head>");
		builder.append("    <meta charset=\"utf-8\">");
		builder.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">");
		builder.append("    <link rel=\"stylesheet\" href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" integrity=\"sha384-JcKb8q3iqJ61gNV9KGb8thSsNjpSL0n8PARn9HuZOnIxN0hoP+VmmDGMN5t9UJ0Z\" crossorigin=\"anonymous\">");
		builder.append("    <title>승인 페이지</title>");
		builder.append("	<script>");
		builder.append("		function cancelConsent() {");
		builder.append("			document.consent_form.reset();");
		builder.append("			document.consent_form.submit();");
		builder.append("		}");
		builder.append("	</script>");
		builder.append("</head>");
		builder.append("<body>");
		builder.append("<div class=\"container\">");
		builder.append("    <div class=\"py-5\">");
		builder.append("        <h1 class=\"text-center\">승인이 필요합니다.</h1>");
		builder.append("    </div>");
		builder.append("    <div class=\"row\">");
		builder.append("        <div class=\"col text-center\">");
		builder.append("            <p><span class=\"font-weight-bold text-primary\">클라이언트(" + clientId + ")</span>가 <span class=\"font-weight-bold\">" + principal.getName() + "의 리소스에 접근하기를 원합니다.</span></p>");
		builder.append("        </div>");
		builder.append("    </div>");
		builder.append("    <div class=\"row pb-3\">");
		builder.append("        <div class=\"col text-center\">");
		builder.append("            <p>다음 권한이 클라이언트 애플리케이션에 의해 요청되었습니다.<br/>검토후 승인해주세요</p>");
		builder.append("        </div>");
		builder.append("    </div>");
		builder.append("    <div class=\"row\">");
		builder.append("        <div class=\"col text-center\">");
		builder.append("            <form name=\"consent_form\" method=\"post\" action=\"" + request.getRequestURI() + "\">");
		builder.append("                <input type=\"hidden\" name=\"client_id\" value=\"" + clientId + "\">");
		builder.append("                <input type=\"hidden\" name=\"state\" value=\"" + state + "\">");
		if (userCode != null) {
			builder.append("                <input type=\"hidden\" name=\"user_code\" value=\"" + userCode + "\">");
		}

		for (String scope : scopesToAuthorize) {
			builder.append("                <div class=\"form-group form-check py-1\">");
			builder.append("                    <input class=\"form-check-input\" type=\"checkbox\" name=\"scope\" value=\"" + scope + "\" id=\"" + scope + "\">");
			builder.append("                    <label class=\"form-check-label\" for=\"" + scope + "\">" + scope + "</label>");
			builder.append("                </div>");
		}

		if (!scopesPreviouslyAuthorized.isEmpty()) {
			builder.append("                <p>이 애플리케이션에 대해 이미 다음 권한을 허가받았습니다.</p>");
			for (String scope : scopesPreviouslyAuthorized) {
				builder.append("                <div class=\"form-group form-check py-1\">");
				builder.append("                    <input class=\"form-check-input\" type=\"checkbox\" name=\"scope\" id=\"" + scope + "\" checked disabled>");
				builder.append("                    <label class=\"form-check-label\" for=\"" + scope + "\">" + scope + "</label>");
				builder.append("                </div>");
			}
		}

		builder.append("                <div class=\"form-group pt-3\">");
		builder.append("                    <button class=\"btn btn-primary btn-lg\" type=\"submit\" id=\"submit-consent\">승인</button>");
		builder.append("                    <button class=\"btn btn-primary btn-lg\" type=\"button\" onclick=\"cancelConsent();\" id=\"cancel-consent\">취소</button>");
		builder.append("                </div>");
		builder.append("                <div class=\"form-group\">");

		builder.append("                </div>");
		builder.append("            </form>");
		builder.append("        </div>");
		builder.append("    </div>");
		builder.append("</div>");
		builder.append("</body>");
		builder.append("</html>");

		return builder.toString();
	}
}
