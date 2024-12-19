package com.multi.oauth10server.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.multi.oauth10server.model.UsersVO;

@Repository("UsersDAO")
public class UsersDAOImpl implements UsersDAO {
	
	@Autowired
	private SqlSession sqlSession;

	@Override
	public void createUser(UsersVO usersVO) throws Exception {
		sqlSession.insert("users.createUser", usersVO);
	}

	@Override
	public UsersVO selectUser(UsersVO usersVO) throws Exception {
		return sqlSession.selectOne("users.selectUserByUserID", usersVO);
	}

	@Override
	public int nextUserNo() throws Exception {
		int nextNo= 0;
		try {
			nextNo = sqlSession.selectOne("users.getNextNo");
		} catch (RuntimeException e) { }
		return nextNo;
	}

	@Override
	public UsersVO selectUserByUserNo(long userno) throws Exception {
		return sqlSession.selectOne("users.selectUserByUserNo", userno);
	}
}
