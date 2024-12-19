package com.multi.oauth10server.service;

import org.springframework.stereotype.Service;

import com.multi.oauth10server.dao.UsersDAO;
import com.multi.oauth10server.model.UsersVO;

import jakarta.annotation.Resource;

@Service("UsersService")
public class UsersServiceImpl implements UsersService {

	@Resource(name="UsersDAO")
	private UsersDAO usersDAO;
	
	@Override
	public void createUsers(UsersVO usersVO) throws Exception {
		usersVO.setUserno(usersDAO.nextUserNo());
		usersDAO.createUser(usersVO);
	}

	@Override
	public UsersVO selectUsers(UsersVO usersVO) throws Exception {
		
		return usersDAO.selectUser(usersVO);
	}

	@Override
	public UsersVO selectUserByUserNo(long userno) throws Exception {
		return usersDAO.selectUserByUserNo(userno);
	}

}
