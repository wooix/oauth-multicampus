package com.multi.oauth10client.util;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class TokenSender {

	private HttpClient httpClient;

	public static final int TYPE_PARAM = 0;
	public static final int TYPE_HEADER = 1;

	private int type;

	public TokenSender() {

		this(TYPE_PARAM);
	}

	public TokenSender(int type) {
		this.type = type;
		this.httpClient = new HttpClient();
	}

	public void sendHttpClient(BasicTokenVO vo) throws Exception {

		System.out.println("sendHttpClient ");

		String responseText = null;

		if (vo.getClass().getName().equals("com.multi.oauth10client.util.RequestTokenVO")) {

			responseText = sendToRequest(type, (RequestTokenVO) vo);
			saveResultToRequestTokenVO(responseText, (RequestTokenVO) vo);

		} else if (vo.getClass().getName().equals("com.multi.oauth10client.util.AccessTokenVO")) {

			responseText = sendToAccess(type, (AccessTokenVO) vo);
			saveResultToAccessTokenVO(responseText, (AccessTokenVO) vo);

		} else if (vo.getClass().getName().equals("com.multi.oauth10client.util.ResourceTokenVO")) {

			responseText = sendToResource(type, (ResourceTokenVO) vo);
			saveResultResourceTokenVO(responseText, (ResourceTokenVO) vo);
		}
	}

	private void saveResultResourceTokenVO(String responseText, ResourceTokenVO vo) {
		vo.setResult(responseText);
	}

	// RequestToken 요청시 요청정보 생성
	protected String sendToRequest(int type2, RequestTokenVO vo) {

		if (type2 == TYPE_PARAM) {
			return requestRequestTokenWithParamType(vo);
		} else if (type2 == TYPE_HEADER) {
			return requestRequestTokenWithHeaderType(vo);
		}

		return null;
	}

	// AccessToken 요청시 요청정보 생성
	protected String sendToAccess(int type2, AccessTokenVO vo) {

		if (type2 == TYPE_PARAM) {
			return requestAccessTokenWithParamType(vo);
		} else if (type2 == TYPE_HEADER) {
			return requestAccessTokenWithHeaderType(vo);
		}

		return null;
	}

	// 제한된 리소스에 접근할 사용할 요청정보 생성
	protected String sendToResource(int type2, ResourceTokenVO vo) {

		if (type2 == TYPE_PARAM) {
			return requestResourceTokenWithParamType(vo);
		} else if (type2 == TYPE_HEADER) {
			return requestResourceTokenWithHeaderType(vo);
		}

		return null;
	}

	// 프로바이더로부터 전달된 RequestToken & Secret 결과값을 RequestTokenVO에 저장
	public void saveResultToRequestTokenVO(String responseText, RequestTokenVO vo) throws Exception {

		String[] tokens = responseText.split("&");

		for (String token : tokens) {

			String[] keyValue = token.split("=");
			// oauth_token
			if (keyValue[0].equals(OAuthConstant.OAUTH_TOKEN)) {
				vo.setRequestOauthToken(keyValue[1]);
			} else if (keyValue[0].equals(OAuthConstant.OAUTH_TOKEN_SECRET)) {
				vo.setRequestOauthTokenSecret(keyValue[1]);
			} else if (keyValue[0].equals(OAuthConstant.OAUTH_VERIFIER)) {
				vo.setVerifier(keyValue[1]);
			}
		}
	}

	// 프로바이더로부터 전달된 AccessToken & Secret 결과값을 AccessTokenVO에 저장
	private void saveResultToAccessTokenVO(String responseText, AccessTokenVO vo) {
		String[] tokens = responseText.split("&");

		for (String token : tokens) {

			String[] keyValue = token.split("=");
			// oauth_token
			if (keyValue[0].equals(OAuthConstant.OAUTH_TOKEN)) {
				vo.setRequestOauthToken(keyValue[1]);
			} else if (keyValue[0].equals(OAuthConstant.OAUTH_TOKEN_SECRET)) {
				vo.setRequestOauthTokenSecret(keyValue[1]);
			} else if (keyValue[0].equals(OAuthConstant.OAUTH_VERIFIER)) {
				vo.setVerifier(keyValue[1]);
			} else if (keyValue[0].equals("user_id")) {
				vo.setUserId(Long.parseLong(keyValue[1]));
			} else if (keyValue[0].equals("screen_name")) {
				vo.setScreenName(keyValue[1]);
			}
		}

	}

	protected String requestAccessTokenWithParamType(RequestTokenVO vo) {
		System.out.println("@@@@ requestAccessTokenWithParamType : " + vo.getRequestURL());

		HttpMethod method = null;
		if (vo.getMethod().equals("GET")) {
			StringBuilder builder = new StringBuilder();
			builder.append(OAuthConstant.OAUTH_NONCE + "=" + OAuthUtil.encode(vo.getNonce()) + "&");
			builder.append(
					OAuthConstant.OAUTH_SIGNATURE_METHOD + "=" + OAuthUtil.encode(vo.getSignatureMethod()) + "&");
			builder.append(OAuthConstant.OAUTH_TIMESTAMP + "=" + OAuthUtil.encode(vo.getTimestamp()) + "&");
			builder.append(OAuthConstant.OAUTH_CONSUMER_KEY + "=" + OAuthUtil.encode(vo.getConsumerKey()) + "&");
			builder.append(OAuthConstant.OAUTH_TOKEN + "=" + OAuthUtil.encode(vo.getRequestOauthToken()) + "&");
			builder.append(OAuthConstant.OAUTH_VERIFIER + "=" + OAuthUtil.encode(vo.getVerifier()) + "&");
			builder.append(OAuthConstant.OAUTH_SIGNATURE + "=" + OAuthUtil.encode(vo.getSignature()) + "&");
			builder.append(OAuthConstant.OAUTH_VERSION + "=" + OAuthUtil.encode(vo.getVersion()));

			method = new GetMethod(vo.getRequestURL() + "?" + builder.toString());
		} else if (vo.getMethod().equals("POST")) {
			method = new PostMethod(vo.getRequestURL());

			((PostMethod) method).addParameter(OAuthConstant.OAUTH_NONCE, vo.getNonce());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_SIGNATURE_METHOD, vo.getSignatureMethod());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_TIMESTAMP, vo.getTimestamp());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_CONSUMER_KEY, vo.getConsumerKey());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_TOKEN, vo.getRequestOauthToken());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_VERIFIER, vo.getVerifier());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_SIGNATURE, vo.getSignature());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_VERSION, vo.getVersion());
		}

		String result = null;
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
			System.out.println("@@@@ result :" + result);
		} catch (Exception e) {
			e.getMessage();
			return e.getMessage();
		}

		return result;
	}

	protected String requestAccessTokenWithHeaderType(AccessTokenVO vo) {

		System.out.println("requestAccessTokenWithHeaderType");

		String result = null;

		HttpMethod method = null;

		if (vo.getMethod().equals("GET")) {
			method = new GetMethod(vo.getRequestURL());

		} else if (vo.getMethod().equals("POST")) {
			method = new PostMethod(vo.getRequestURL());

		}

		method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		Header header = new Header();
		header.setName("Authorization");

		StringBuilder builder = new StringBuilder();
		builder.append("OAuth");
		builder.append(" " + OAuthConstant.OAUTH_NONCE + "=\"" + vo.getNonce() + "\",");

		builder.append(" " + OAuthConstant.OAUTH_SIGNATURE_METHOD + "=\"" + vo.getSignatureMethod() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_TIMESTAMP + "=\"" + vo.getTimestamp() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_CONSUMER_KEY + "=\"" + vo.getConsumerKey() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_TOKEN + "=\"" + OAuthUtil.encode(vo.getRequestOauthToken()) + "\",");

		builder.append(" " + OAuthConstant.OAUTH_VERIFIER + "=\"" + OAuthUtil.encode(vo.getVerifier()) + "\",");
		builder.append(" " + OAuthConstant.OAUTH_SIGNATURE + "=\"" + OAuthUtil.encode(vo.getSignature()) + "\",");
		builder.append(" " + OAuthConstant.OAUTH_VERSION + "=\"" + vo.getVersion() + "\"");

		header.setValue(builder.toString());

		method.addRequestHeader(header);

		// System.out.println(header.toString());

		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.getMessage();
			return e.getMessage();
		}

		return result;
	}

	protected String requestRequestTokenWithParamType(RequestTokenVO vo) {

		System.out.println("requestRequestTokenWithParam :  " + vo.getRequestURL());

		HttpMethod method = null;

		if (vo.getMethod().equals("GET")) {
			StringBuilder builder = new StringBuilder();
			builder.append(OAuthConstant.OAUTH_NONCE + "=" + OAuthUtil.encode(vo.getNonce()) + "&");
			builder.append(OAuthConstant.OAUTH_CALLBACK + "=" + OAuthUtil.encode(vo.getCallbackURL()) + "&");
			builder.append(OAuthConstant.OAUTH_SIGNATURE_METHOD + "=" + vo.getSignatureMethod() + "&");
			builder.append(OAuthConstant.OAUTH_TIMESTAMP + "=" + OAuthUtil.encode(vo.getTimestamp()) + "&");
			builder.append(OAuthConstant.OAUTH_CONSUMER_KEY + "=" + OAuthUtil.encode(vo.getConsumerKey()) + "&");
			builder.append(OAuthConstant.OAUTH_SIGNATURE + "=" + OAuthUtil.encode(vo.getSignature()) + "&");
			builder.append(OAuthConstant.OAUTH_VERSION + "=" + OAuthUtil.encode(vo.getVersion()));

			method = new GetMethod(vo.getRequestURL() + "?" + builder.toString());
			System.out.println(vo.getRequestURL() + "?" + builder.toString());

		} else if (vo.getMethod().equals("POST")) {
			method = new PostMethod(vo.getRequestURL());

			((PostMethod) method).addParameter(OAuthConstant.OAUTH_NONCE, vo.getNonce());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_CALLBACK, vo.getCallbackURL());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_SIGNATURE_METHOD, vo.getSignatureMethod());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_TIMESTAMP, vo.getTimestamp());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_CONSUMER_KEY, vo.getConsumerKey());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_SIGNATURE, vo.getSignature());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_VERSION, vo.getVersion());
		}

		String result = null;
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.getMessage();
			return e.getMessage();
		}

		return result;
	}

	protected String requestRequestTokenWithHeaderType(RequestTokenVO vo) {

		System.out.println("requestRequestTokenWithHeader " + vo.getRequestURL());

		String result = null;

		HttpMethod method = null;

		if (vo.getMethod().equals("GET")) {
			method = new GetMethod(vo.getRequestURL());

		} else if (vo.getMethod().equals("POST")) {
			method = new PostMethod(vo.getRequestURL());
		}

		method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		Header header = new Header();

		header.setName("Authorization");

		StringBuilder builder = new StringBuilder();
		builder.append("OAuth");
		builder.append(" " + OAuthConstant.OAUTH_NONCE + "=\"" + vo.getNonce() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_CALLBACK + "=\"" + OAuthUtil.encode(vo.getCallbackURL()) + "\",");
		builder.append(" " + OAuthConstant.OAUTH_SIGNATURE_METHOD + "=\"" + vo.getSignatureMethod() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_TIMESTAMP + "=\"" + vo.getTimestamp() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_CONSUMER_KEY + "=\"" + vo.getConsumerKey() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_SIGNATURE + "=\"" + OAuthUtil.encode(vo.getSignature()) + "\",");
		builder.append(" " + OAuthConstant.OAUTH_VERSION + "=\"" + vo.getVersion() + "\"");

		header.setValue(builder.toString());

		method.addRequestHeader(header);

		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.getMessage();
			return e.getMessage();
		}

		return result;
	}

	protected String requestResourceTokenWithParamType(ResourceTokenVO vo) {
		System.out.println("requestResourceTokenWithParam :  " + vo.getRequestURL());

		HttpMethod method = null;
		if (vo.getMethod().equals("GET")) {
			StringBuilder builder = new StringBuilder();
			builder.append(OAuthConstant.OAUTH_TOKEN + "=" + OAuthUtil.encode(vo.getRequestOauthToken()) + "&");
			builder.append(OAuthConstant.OAUTH_CONSUMER_KEY + "=" + OAuthUtil.encode(vo.getConsumerKey()) + "&");
			builder.append(
					OAuthConstant.OAUTH_SIGNATURE_METHOD + "=" + OAuthUtil.encode(vo.getSignatureMethod()) + "&");
			builder.append(OAuthConstant.OAUTH_TIMESTAMP + "=" + OAuthUtil.encode(vo.getTimestamp()) + "&");
			builder.append(OAuthConstant.OAUTH_NONCE + "=" + OAuthUtil.encode(vo.getNonce()) + "&");
			builder.append(OAuthConstant.OAUTH_VERSION + "=" + OAuthUtil.encode(vo.getVersion()) + "&");
			builder.append(OAuthConstant.OAUTH_SIGNATURE + "=" + OAuthUtil.encode(vo.getSignature()));
			if (vo.getRequestURL().indexOf("?") < 0)
				method = new GetMethod(vo.getRequestURL() + "?" + builder.toString());
			else
				method = new GetMethod(vo.getRequestURL() + "&" + builder.toString());

		} else if (vo.getMethod().equals("POST")) {
			method = new PostMethod(vo.getRequestURL());

			((PostMethod) method).addParameter(OAuthConstant.OAUTH_TOKEN, vo.getRequestOauthToken());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_CONSUMER_KEY, vo.getConsumerKey());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_SIGNATURE_METHOD, vo.getSignatureMethod());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_TIMESTAMP, vo.getTimestamp());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_NONCE, vo.getNonce());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_VERSION, vo.getVersion());
			((PostMethod) method).addParameter(OAuthConstant.OAUTH_SIGNATURE, vo.getSignature());

		}

		String result = null;
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.getMessage();
			return e.getMessage();
		}

		return result;
	}

	protected String requestResourceTokenWithHeaderType(ResourceTokenVO vo) {

		System.out.println("requestResourceTokenWithHeaderType");

		String result = null;

		HttpMethod method = null;

		if (vo.getMethod().equals("GET")) {
			method = new GetMethod(vo.getRequestURL());

		} else if (vo.getMethod().equals("POST")) {
			method = new PostMethod(vo.getRequestURL());
		}

		method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");

		Header header = new Header();

		header.setName("Authorization");

		StringBuilder builder = new StringBuilder();
		builder.append("OAuth ");
		builder.append(" " + OAuthConstant.OAUTH_TOKEN + "=\"" + vo.getRequestOauthToken() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_CONSUMER_KEY + "=\"" + vo.getConsumerKey() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_SIGNATURE_METHOD + "=\"" + vo.getSignatureMethod() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_TIMESTAMP + "=\"" + vo.getTimestamp() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_NONCE + "=\"" + vo.getNonce() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_VERSION + "=\"" + vo.getVersion() + "\",");
		builder.append(" " + OAuthConstant.OAUTH_SIGNATURE + "=\"" + OAuthUtil.encode(vo.getSignature()) + "\",");

		header.setValue(builder.toString());

		method.addRequestHeader(header);

		System.out.println(header.toString());

		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.getMessage();
			return e.getMessage();
		}
		return result;

	}

}
