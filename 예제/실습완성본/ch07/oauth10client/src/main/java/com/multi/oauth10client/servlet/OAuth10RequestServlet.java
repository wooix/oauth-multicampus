package com.multi.oauth10client.servlet;

import java.io.IOException;

import org.springframework.web.servlet.HttpServletBean;

import com.multi.oauth10client.Setup;
import com.multi.oauth10client.util.OAuthConstant;
import com.multi.oauth10client.util.RequestTokenVO;
import com.multi.oauth10client.util.TokenSender;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OAuth10RequestServlet extends HttpServletBean {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			RequestTokenVO vo = new RequestTokenVO();
			vo.setMethod("GET");
			vo.setRequestURL(Setup.REQUEST_TOKEN_URL);
			vo.setConsumerKey(Setup.CONSUMER_KEY);
			vo.setConsumerSecretKey(Setup.CONSUMER_SECRET);
			vo.setCallbackURL(Setup.CALLBACK_URL);
			vo.sign();

			TokenSender sender = new TokenSender(TokenSender.TYPE_PARAM);
			sender.sendHttpClient(vo);
			String RT = vo.getRequestOauthToken();
			String RTS = vo.getRequestOauthTokenSecret();
			HttpSession session = req.getSession();
			session.setAttribute("RT", RT);
			session.setAttribute("RTS", RTS);
			resp.sendRedirect(Setup.AUTHORIZE_URL + "?" + OAuthConstant.OAUTH_TOKEN + "=" + RT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
