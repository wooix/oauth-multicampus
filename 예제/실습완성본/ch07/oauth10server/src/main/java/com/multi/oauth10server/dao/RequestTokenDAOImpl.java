package com.multi.oauth10server.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.multi.oauth10server.model.RequestTokenVO;

@Repository("RequestTokenDAO")
public class RequestTokenDAOImpl implements RequestTokenDAO {
	
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public void createRequestToken(RequestTokenVO requestTokenVO)  throws Exception{
		sqlSession.insert("requestToken.create", requestTokenVO);
	}

	@Override
	public void deleteRequestToken(String requestToken) throws Exception {
		sqlSession.delete("requestToken.delete", requestToken);
		sqlSession.delete("requestToken.deleteExpired");
	}

	@Override
	public RequestTokenVO selectRequestToken(String requestToken) throws Exception {
		return sqlSession.selectOne("requestToken.select", requestToken);
	}

	@Override
	public void updateUserNo(RequestTokenVO requestTokenVO) throws Exception {
		sqlSession.update("requestToken.updateUserNo", requestTokenVO);
	}

}
