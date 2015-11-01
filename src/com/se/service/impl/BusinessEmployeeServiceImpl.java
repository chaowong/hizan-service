package com.se.service.impl;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.se.dao.BusinessEmployeeDao;
import com.se.dao.base.BaseDao;
import com.se.entity.BusinessEmployee;
import com.se.service.BusinessEmployeeService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class BusinessEmployeeServiceImpl extends BaseServiceImpl<BusinessEmployee> implements BusinessEmployeeService{
	@Resource
	private BusinessEmployeeDao businessemployeeDao;	
	public BaseDao<BusinessEmployee> getBaseDao() {
		return this.businessemployeeDao;
	}
	
}
