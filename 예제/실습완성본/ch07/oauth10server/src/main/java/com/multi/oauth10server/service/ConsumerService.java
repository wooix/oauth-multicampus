package com.multi.oauth10server.service;

import java.util.List;

import com.multi.oauth10server.model.ConsumerVO;

public interface ConsumerService {
	void createConsumer(ConsumerVO oAuthKeyVO) throws Exception;
	void deleteConsumer(String consumerKey) throws Exception;
	ConsumerVO selectByConsumerKey(String consumerKey) throws Exception;
	List<ConsumerVO> selectByUserId(String userId) throws Exception;
}
