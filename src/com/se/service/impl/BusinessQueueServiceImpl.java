package com.se.service.impl;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.se.dao.BusinessQueueDao;
import com.se.dao.base.BaseDao;
import com.se.entity.BusinessEmployee;
import com.se.entity.BusinessQueue;
import com.se.service.BusinessQueueService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class BusinessQueueServiceImpl extends BaseServiceImpl<BusinessQueue> implements BusinessQueueService{
	@Resource
	private BusinessQueueDao businessqueueDao;	
	public BaseDao<BusinessQueue> getBaseDao() {
		return this.businessqueueDao;
	}
	
	public List<BusinessQueue> listByPerform(Map<?, ?> map){
		return this.businessqueueDao.listByPerform(map);
	}
	
	public List<BusinessQueue> listByEmployeePerform(Map<?, ?> map){
		return this.businessqueueDao.listByEmployeePerform(map);
	}
	
	//统计每个用户前面排队的人数 
	public int countFrontBy(Map<?, ?> map){
		return this.businessqueueDao.countFrontBy(map);
	}
	
	public List<BusinessQueue> listTodayBy(Map<?, ?>  map){
		return this.businessqueueDao.listTodayBy(map);
	}
}
