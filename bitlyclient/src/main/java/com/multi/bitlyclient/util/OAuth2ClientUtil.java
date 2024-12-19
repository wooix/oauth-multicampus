package com.multi.bitlyclient.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuth2ClientUtil {

	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
	}

	public static String getParamStringFromMap(HashMap<String, String> map) {
		String strout = "";
		try {
			int i = 0;
			for (String key : map.keySet()) {
				if (i != 0)
					strout += "&";
				strout += key + "=" + URLEncoder.encode(map.get(key), "utf-8");
				i++;
			}
		} catch (Exception ex) {
			strout = "";
		}
		return strout;
	}

	public static HashMap<String, String> getMapFromParamString(String paramString)
			throws UnsupportedEncodingException {
		String[] params = paramString.split("&");
		HashMap<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < params.length; i++) {
			String[] kv = params[i].split("=");
			map.put(kv[0], URLDecoder.decode(kv[1], "utf-8"));
		}
		return map;
	}

	public static String getJSONFromObject(Object obj) {
		try {
			StringWriter sw = new StringWriter(); // serialize
			mapper.writeValue(sw, obj);
			sw.close();

			return sw.getBuffer().toString();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T getObjectFromJSON(String json, Class<T> classOfT) {
		try {
			return mapper.readValue(json.getBytes("UTF-8"), classOfT);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// hex to byte[]
	public static byte[] hexToBinary(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}

		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	// byte[] to hex
	public static String binaryToHex(byte[] ba) {
		if (ba == null || ba.length == 0) {
			return null;
		}

		StringBuffer sb = new StringBuffer(ba.length * 2);
		String hexNumber;
		for (int x = 0; x < ba.length; x++) {
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);

			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	public static String generateRandomState() {

		SecureRandom secureRandom;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(secureRandom.generateSeed(256));
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] dig = digest.digest((secureRandom.nextLong() + "").getBytes());
			return binaryToHex(dig).substring(0, 5);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}

	}

	// Authorization : Basic XXXXXXXXXX
	public static String generateBasicAuthHeaderString(String client_id, String client_secret) {
		try {
			String base = "";
			if (client_secret == null || client_secret.equals("")) {
				base = URLEncoder.encode(client_id, "utf-8");
			} else {
				base = URLEncoder.encode(client_id, "utf-8") + ":" + URLEncoder.encode(client_secret, "utf-8");
			}

			return "Basic " + Base64.encodeBase64(base.getBytes("UTF-8"));

		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String generateBearerTokenHeaderString(String access_token) {
		return "Bearer " + access_token;
	}

}
