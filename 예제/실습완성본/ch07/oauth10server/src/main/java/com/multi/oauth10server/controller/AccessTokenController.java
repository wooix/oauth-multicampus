package com.multi.oauth10server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thinker.oauth.generator.TokenGenerator;
import org.thinker.oauth.parameter.AccessTokenParam;
import org.thinker.oauth.util.OAuthMsgConstants;

import com.multi.oauth10server.model.AccessTokenVO;
import com.multi.oauth10server.model.ConsumerVO;
import com.multi.oauth10server.model.RequestTokenVO;
import com.multi.oauth10server.model.UsersVO;
import com.multi.oauth10server.service.ConsumerService;
import com.multi.oauth10server.service.RequestTokenService;
import com.multi.oauth10server.service.UsersService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/oauth/access_token")
public class AccessTokenController {

	@Autowired
	private RequestTokenService requestTokenService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private ConsumerService consumerService;

	@GetMapping
	public ModelAndView getAccessToken(HttpServletRequest request) throws Exception {
		// 1. 파라미터 파싱
		AccessTokenParam param = new AccessTokenParam(request);
		// 1.1 DB테이블에서 consumer, requestToken, User 정보 읽음(ConsumerSecret, Password,
		// Verifier 정보 획득)
		ConsumerVO consumerVO = consumerService.selectByConsumerKey(param.getConsumerKey());
		RequestTokenVO requestTokenVO = requestTokenService.getRequestToken(param.getRequestToken());
		UsersVO usersVO = usersService.selectUserByUserNo(requestTokenVO.getUserNo());

		ModelAndView mav = new ModelAndView();
		mav.setViewName("access_token");

		// 2. signature validation!! 유효하지 않으면 예외 발생!
		param.validateRequestToken(requestTokenVO.getRequestTokenSecret(), requestTokenVO.getVerifier(),
				consumerVO.getConsumerSecret());
		// 2.1 유효하다면 RequestToken 테이블의 레코드 삭제 : 임시 TOken이므로..
		requestTokenService.deleteRequestToken(requestTokenVO.getRequestToken());

		// 3. AccessToken 생성
		AccessTokenVO accessTokenVO = TokenGenerator.generateAccessToken(usersVO, consumerVO);

		StringBuilder builder = new StringBuilder();
		builder.append(OAuthMsgConstants.OAUTH_TOKEN + "=" + accessTokenVO.getAccessToken() + "&");
		builder.append(OAuthMsgConstants.OAUTH_TOKEN_SECRET + "=" + accessTokenVO.getAccessTokenSecret() + "&");
		builder.append("userno=" + accessTokenVO.getUserNo() + "&");
		builder.append("userid=" + accessTokenVO.getUserID());

		mav.addObject("message", builder.toString());

		return mav;

	}
}
