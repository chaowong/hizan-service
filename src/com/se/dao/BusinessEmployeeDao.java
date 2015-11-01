package com.se.dao;
import java.util.List;
import java.util.Map;

import com.se.dao.base.BaseDao;
import com.se.entity.BusinessEmployee;
public interface BusinessEmployeeDao extends BaseDao<BusinessEmployee>{
	
	public List<BusinessEmployee> listTodayBy(Map<?, ?>  map);
}
