package com.multi.oauth10server.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.multi.oauth10server.model.OAuthKeyVO;

@Repository("OAuthKeyDAO")
public class OAuthKeyDAOImpl implements OAuthKeyDAO {
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void createOAuthToken(OAuthKeyVO oAuthKeyVO) throws Exception  {
		sqlSession.insert("oAuthKey.create", oAuthKeyVO);
	}

	@Override
	public void deleteOAuthToken(String consumerKey) throws Exception  {
		sqlSession.delete("oAuthKey.delete", consumerKey);
	}

	@Override
	public OAuthKeyVO selectByConsumerKey(String consumerKey) throws Exception  {
		return sqlSession.selectOne("oAuthKey.selectByConsumerKey", consumerKey);
	}

	@Override
	public List<OAuthKeyVO> selectByUserId(String userId) throws Exception  {
		return sqlSession.selectList("oAuthKey.selectByUserID", userId);
	}

}
