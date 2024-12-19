package com.multi.bitlyclient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.multi.bitlyclient.util.OAuth2ClientUtil;
import com.multi.bitlyclient.util.Settings;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/callback")
public class CallbackController {
	
	@GetMapping()
	public String requestCallback(HttpSession session, Model model, 
			@RequestParam(value = "code") String code)
			throws ClientProtocolException, IOException {
		HttpPost httpPost = new HttpPost(Settings.ACCES_TOKEN_URL);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("client_id", Settings.CLIENT_ID));
		nameValuePairs.add(new BasicNameValuePair("client_secret", Settings.CLIENT_SECRET));
		nameValuePairs.add(new BasicNameValuePair("redirect_uri", Settings.REDIRECT_URI));
		nameValuePairs.add(new BasicNameValuePair("grant_type", "authorization_code"));
		nameValuePairs.add(new BasicNameValuePair("code", code));
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, StandardCharsets.UTF_8));

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = httpClient.execute(httpPost);

		String body = "";

		if (response.getStatusLine().getStatusCode() == 200) {
			ResponseHandler<String> handler = new BasicResponseHandler();
			body = handler.handleResponse(response);
		    HashMap<String,String> tokenMap = OAuth2ClientUtil.getMapFromParamString(body);
		    session.setAttribute("access_token", tokenMap.get("access_token"));
		    return "redirect:/main";
		} else {
			model.addAttribute("message", "인증실패");
			model.addAttribute("body", body);
		}
		return "error";
	}
}
