package com.se.service;
import java.util.List;
import java.util.Map;

import com.se.entity.BusinessEmployee;
import com.se.entity.BusinessQueue;
import com.se.service.base.BaseService;
public interface BusinessQueueService extends BaseService<BusinessQueue>{

	/**查询 未取消和未评价的队列
	 * @param map
	 * @return
	 */
	public List<BusinessQueue> listByPerform(Map<?, ?> map);//  
	public List<BusinessQueue> listByEmployeePerform(Map<?, ?> map);//职工能看到的两种状态的排号
	public int countFrontBy(Map<?, ?> map);//统计每个用户前面排队的人数 
	public List<BusinessQueue> listTodayBy(Map<?, ?>  map);//
}
