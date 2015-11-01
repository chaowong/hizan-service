package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.UserDao;
import com.se.dao.base.BaseDao;
import com.se.entity.User;
import com.se.service.UserService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{
	@Resource
	private UserDao userDao;	
	public BaseDao<User> getBaseDao() {
		return this.userDao;
	}
}
