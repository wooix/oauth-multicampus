package com.multi.oauth10server.dao;

import com.multi.oauth10server.model.UsersVO;

public interface UsersDAO {
	void createUser(UsersVO usersVO) throws Exception;
	UsersVO selectUser(UsersVO usersVO) throws Exception;
	UsersVO selectUserByUserNo(long userno) throws Exception;
	int nextUserNo() throws Exception;
	
}
