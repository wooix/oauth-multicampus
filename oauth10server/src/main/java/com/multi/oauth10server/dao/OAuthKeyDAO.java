package com.multi.oauth10server.dao;

import java.util.List;

import com.multi.oauth10server.model.OAuthKeyVO;

public interface OAuthKeyDAO {
	void createOAuthToken(OAuthKeyVO oAuthKeyVO) throws Exception;
	void deleteOAuthToken(String consumerKey) throws Exception;
	OAuthKeyVO selectByConsumerKey(String consumerKey) throws Exception;
	List<OAuthKeyVO> selectByUserId(String userId) throws Exception;
}
