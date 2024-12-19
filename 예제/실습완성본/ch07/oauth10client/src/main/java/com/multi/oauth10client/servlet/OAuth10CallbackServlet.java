package com.multi.oauth10client.servlet;

import java.io.IOException;

import org.springframework.web.servlet.HttpServletBean;

import com.multi.oauth10client.Setup;
import com.multi.oauth10client.util.AccessTokenVO;
import com.multi.oauth10client.util.TokenSender;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OAuth10CallbackServlet extends HttpServletBean {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			HttpSession session = req.getSession();
			String RT = (String) session.getAttribute("RT");
			String RTS = (String) session.getAttribute("RTS");

			AccessTokenVO vo = new AccessTokenVO(req.getQueryString());
			vo.setMethod("GET");
			vo.setRequestURL(Setup.ACCESS_TOKEN_URL);
			vo.setConsumerKey(Setup.CONSUMER_KEY);
			vo.setConsumerSecretKey(Setup.CONSUMER_SECRET);
			vo.setRequestOauthToken(RT);
			vo.setRequestOauthTokenSecret(RTS);
			// vo.setVerifier(vo.getVerifier());
			vo.sign();

			TokenSender sender = new TokenSender(TokenSender.TYPE_PARAM);
			sender.sendHttpClient(vo);
			String AT = vo.getRequestOauthToken();
			String ATS = vo.getRequestOauthTokenSecret();
			session.setAttribute("AT", AT);
			session.setAttribute("ATS", ATS);
			resp.sendRedirect("resource");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
