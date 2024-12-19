package com.multi.oauth10server.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.multi.oauth10server.model.ConsumerVO;

@Repository("ConsumerDAO")
public class ConsumerDAOImpl implements ConsumerDAO {

	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void createOAuthToken(ConsumerVO oAuthKeyVO) throws Exception  {
		sqlSession.insert("Consumer.create", oAuthKeyVO);
	}

	@Override
	public void deleteOAuthToken(String consumerKey) throws Exception  {
		sqlSession.delete("Consumer.delete", consumerKey);
	}

	@Override
	public ConsumerVO selectByConsumerKey(String consumerKey) throws Exception  {
		return sqlSession.selectOne("Consumer.selectByConsumerKey", consumerKey);
	}

	@Override
	public List<ConsumerVO> selectByUserId(String userId) throws Exception  {
		return sqlSession.selectList("Consumer.selectByUserID", userId);
	}

}
