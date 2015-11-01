package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.PermissionDao;
import com.se.dao.base.BaseDao;
import com.se.entity.Permission;
import com.se.service.PermissionService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService{
	@Resource
	private PermissionDao permissionDao;	
	public BaseDao<Permission> getBaseDao() {
		return this.permissionDao;
	}
}
