package com.se.service;
import java.util.List;
import java.util.Map;

import com.se.entity.Business;
import com.se.service.base.BaseService;
import com.deer.core.utils.Pager;
public interface BusinessService extends BaseService<Business>{

	public List<Business> listByDescription(Map<?, ?> map);// 模糊查询
}
