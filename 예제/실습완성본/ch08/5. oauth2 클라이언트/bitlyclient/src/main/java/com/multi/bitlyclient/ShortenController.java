package com.multi.bitlyclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.multi.bitlyclient.util.OAuth2ClientUtil;
import com.multi.bitlyclient.util.Settings;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/shorten")
public class ShortenController {
	
	@GetMapping()
	public void requestMain(HttpSession session, HttpServletResponse res, 
			@RequestParam(value = "long_url") String long_url) throws Exception {
		
		if (session.getAttribute("access_token") == null) {
			throw new Exception("access_token is null");
		}

		String access_token = (String) session.getAttribute("access_token");
		String bearerToken = OAuth2ClientUtil.generateBearerTokenHeaderString(access_token);

		HttpPost httpPost = new HttpPost(Settings.SHORTEN_API_URL);
		httpPost.setHeader("Authorization", bearerToken);
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Accept", "application/json");

		String json = "{ \"long_url\" : \"" + long_url + "\" } ";
		HttpEntity postEntity = new StringEntity(json, "UTF-8");
		httpPost.setEntity(postEntity);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		CloseableHttpResponse response = httpClient.execute(httpPost);

		String result = "";
		if (response.getStatusLine().getStatusCode() == 200) {
			ResponseHandler<String> handler = new BasicResponseHandler();
			result = handler.handleResponse(response);
			System.out.println(result);
		} else {
			result = "{ \"status\" : \"오류 발생 : " + response.getStatusLine().getStatusCode() + "\" }";
		}
		res.setContentType("application/json");
		res.getWriter().print(result);
		res.getWriter().close();
	}
}
