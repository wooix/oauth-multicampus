package com.multi.oauth10server.dao;

import com.multi.oauth10server.model.RequestTokenVO;

public interface RequestTokenDAO {
	void createRequestToken(RequestTokenVO requestTokenVO) throws Exception;
	void deleteRequestToken(String requestToken) throws Exception;
	RequestTokenVO selectRequestToken(String requestToken) throws Exception;
	void updateUserNo(RequestTokenVO requestTokenVO) throws Exception;
}
