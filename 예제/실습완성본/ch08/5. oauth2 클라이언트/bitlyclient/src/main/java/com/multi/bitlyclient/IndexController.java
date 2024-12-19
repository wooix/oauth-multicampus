package com.multi.bitlyclient;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.multi.bitlyclient.util.OAuth2ClientUtil;
import com.multi.bitlyclient.util.Settings;

@Controller
@RequestMapping(value = "/")
public class IndexController {

	@GetMapping
	public String requestIndex(Model model) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("client_id", Settings.CLIENT_ID);
		map.put("redirect_uri", Settings.REDIRECT_URI);
		map.put("response_type", "code");
		String url = Settings.AUTHORIZE_URL + "?" + OAuth2ClientUtil.getParamStringFromMap(map);
		model.addAttribute("url", url);
		return "index";
	}
}
