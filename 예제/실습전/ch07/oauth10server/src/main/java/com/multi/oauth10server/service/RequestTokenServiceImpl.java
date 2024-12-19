package com.multi.oauth10server.service;

import org.springframework.stereotype.Service;

import com.multi.oauth10server.dao.RequestTokenDAO;
import com.multi.oauth10server.model.RequestTokenVO;

import jakarta.annotation.Resource;

@Service("RequestTokenService")
public class RequestTokenServiceImpl implements RequestTokenService {
	
	@Resource(name="RequestTokenDAO")
	private RequestTokenDAO requestTokenDAO;
	
	@Override
	public void createRequestToken(RequestTokenVO requestTokenVO)
			throws Exception {
		requestTokenDAO.createRequestToken(requestTokenVO);
	}

	@Override
	public void deleteRequestToken(String requestToken) throws Exception {
		requestTokenDAO.deleteRequestToken(requestToken);

	}

	@Override
	public RequestTokenVO getRequestToken(String requestToken) throws Exception {
		return requestTokenDAO.selectRequestToken(requestToken);
	}

	@Override
	public void updateUserNo(RequestTokenVO requestTokenVO) throws Exception {
		requestTokenDAO.updateUserNo(requestTokenVO);
	}

}
