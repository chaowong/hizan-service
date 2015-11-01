package com.se.service.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.se.dao.BusinessDao;
import com.se.dao.base.BaseDao;
import com.se.entity.Business;
import com.se.service.BusinessService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class BusinessServiceImpl extends BaseServiceImpl<Business> implements BusinessService{
	@Resource
	private BusinessDao businessDao;	
	public BaseDao<Business> getBaseDao() {
		return this.businessDao;
	}
	// 模糊查询
	public List<Business> listByDescription(Map<?, ?> map){
		return this.businessDao.listByDescription(map);
	}
}
