package com.multi.oauth10server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.thinker.oauth.util.OAuthException;

import com.multi.oauth10server.model.RequestTokenVO;
import com.multi.oauth10server.model.UsersVO;
import com.multi.oauth10server.service.RequestTokenService;
import com.multi.oauth10server.service.UsersService;
import com.multi.oauth10server.util.SessionUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/oauth/authorize")
public class AuthorizeController {

	@Autowired
	private RequestTokenService requestTokenService;

	@Autowired
	private UsersService usersService;

	@GetMapping
	public ModelAndView authorizeGet(HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView();
		String oauth_token = (String) request.getParameter("oauth_token");
		if (oauth_token != null) {
			// 1. oauth_token 값으로 tbl_request_token 조회하여 레코드가 존재하지 않으면 오류임.
			RequestTokenVO requestTokenVO = requestTokenService.getRequestToken(oauth_token);
			if (requestTokenVO != null) {
				mav.setViewName("authorize");
				mav.addObject("requestTokenVO", requestTokenVO);
			} else {
				throw new OAuthException("Invalid/Expired Token", "E002-2");
			}
		} else {
			throw new OAuthException("missing token parameter", "E002-1");
		}
		return mav;
	}

	@PostMapping
	public ModelAndView authorizePost(HttpSession session, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		// 1. QueryString 값 파싱
		String allow_deny = request.getParameter("allow_deny");
		String oauth_token = request.getParameter("oauth_token");
		String userid = request.getParameter("userid");
		String password = request.getParameter("password");

		// 2. 임시 생성된 RequestToken 값 읽어오기(from tbl_request_token)
		RequestTokenVO tokenVO = (RequestTokenVO) requestTokenService.getRequestToken(oauth_token);
		ModelAndView mav = new ModelAndView();
		if (tokenVO == null) {
			throw new OAuthException("Invalid/Expired Token", "E002-2");
		}

		mav.addObject("requestTokenVO", tokenVO);
		mav.setViewName("authorize");
		if (allow_deny.equals("allow")) {
			UsersVO usersVO = null;
			if (!SessionUtil.isLoginned(session)) {
				UsersVO inputVO = new UsersVO(userid, password, "", 0);
				usersVO = usersService.selectUsers(inputVO);
				if (usersVO != null) {
					SessionUtil.loginUser(session, usersVO);
				} else {
					// 승인 버튼을 눌렀으나 사용자 계정 정보가 올바르지 않다면 다시 authorize 페이지로 이동
					mav.addObject("loginResult", "false");
					mav.setViewName("authorize");
					return mav;
				}
			}
			// RequestTokenTable의 UserNo 필드값을 애플리케이션의 접근을 허용한 사용자의 UserNo로 변경
			tokenVO.setUserNo(SessionUtil.getUserInfo(session).getUserno());
			requestTokenService.updateUserNo(tokenVO);

			// 로그인된 상태에서 App을 승인하면 Callback URL로 이동
			response.sendRedirect(tokenVO.getCallback() + "?oauth_token=" + tokenVO.getRequestToken()
					+ "&oauth_verifier=" + tokenVO.getVerifier());
		} else {
			// 승인거부를 하였다면 임시 생성한 RequestToken값을 삭제하고 승인거부 화면 출력
			requestTokenService.deleteRequestToken(oauth_token);
			mav.setViewName("authorize_error");
			mav.addObject("errorMessage", "사용자가 애플리케이션의 접근을 허용하지 않습니다.");
			SessionUtil.logoutUser(session);
		}
		return mav;

	}

}
