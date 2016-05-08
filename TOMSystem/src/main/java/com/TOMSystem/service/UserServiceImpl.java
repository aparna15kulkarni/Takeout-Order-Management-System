package com.TOMSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TOMSystem.User.User;
import com.TOMSystem.dao.UserDao;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Transactional
	public void add(User user) {
		// TODO Auto-generated method stub
		userDao.add(user);
	}

	@Transactional
	public void edit(User user) {
		// TODO Auto-generated method stub
		userDao.edit(user);
	}

	@Transactional
	public void delete(int userId) {
		// TODO Auto-generated method stub
		userDao.delete(userId);
	}

	@Transactional
	public User getUser(int userId) {
		// TODO Auto-generated method stub
		return userDao.getUser(userId);
	}

	@Transactional
	public List getAllUsers() {
		// TODO Auto-generated method stub
		return userDao.getAllUsers();
	}

}