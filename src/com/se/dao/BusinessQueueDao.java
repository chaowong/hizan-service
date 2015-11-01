package com.se.dao;
import java.util.List;
import java.util.Map;

import com.se.dao.base.BaseDao;
import com.se.entity.BusinessQueue;
public interface BusinessQueueDao extends BaseDao<BusinessQueue>{
	
	public List<BusinessQueue> listByPerform(Map<?, ?> map);//用户能看到的三种状态的排号
	public List<BusinessQueue> listByEmployeePerform(Map<?, ?> map);//职工能看到的两种状态的排号
	public int countFrontBy(Map<?, ?> map);//统计每个用户前面排队的人数 
	public List<BusinessQueue> listTodayBy(Map<?, ?>  map);//
}
