package com.multi.oauth10client.servlet;

import java.io.IOException;

import org.springframework.web.servlet.HttpServletBean;

import com.multi.oauth10client.Setup;
import com.multi.oauth10client.util.ResourceTokenVO;
import com.multi.oauth10client.util.TokenSender;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OAuth10ResourceServlet extends HttpServletBean {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			HttpSession session = req.getSession();
			String AT = (String) session.getAttribute("AT");
			String ATS = (String) session.getAttribute("ATS");
//			System.out.println("## AT : " + AT);
//			System.out.println("## ATS : " + ATS);
			
			ResourceTokenVO vo = new ResourceTokenVO();
			vo.setMethod("GET");
			vo.setRequestURL(Setup.RESOURCE_URL);
			vo.setConsumerKey(Setup.CONSUMER_KEY);
			vo.setConsumerSecretKey(Setup.CONSUMER_SECRET);
			vo.setRequestOauthToken(AT);
			vo.setRequestOauthTokenSecret(ATS);
			vo.sign();

			TokenSender sender = new TokenSender(TokenSender.TYPE_PARAM);

			sender.sendHttpClient(vo);
			String result = vo.getResult();
			resp.setContentType("application/json");
			resp.getWriter().print(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
