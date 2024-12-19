package com.multi.oauth10server.service;

import com.multi.oauth10server.model.UsersVO;

public interface UsersService {
	void createUsers(UsersVO usersVO) throws Exception;
	UsersVO selectUsers(UsersVO usersVO) throws Exception;
	UsersVO selectUserByUserNo(long userno) throws Exception;
}
