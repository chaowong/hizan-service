package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.RoleDao;
import com.se.dao.base.BaseDao;
import com.se.entity.Role;
import com.se.service.RoleService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService{
	@Resource
	private RoleDao roleDao;	
	public BaseDao<Role> getBaseDao() {
		return this.roleDao;
	}
}
