package com.se.dao;
import java.util.List;
import java.util.Map;

import com.se.dao.base.BaseDao;
import com.se.entity.Business;
public interface BusinessDao extends BaseDao<Business>{
	public List<Business> listByDescription(Map<?, ?> map);// 模糊查询
}
