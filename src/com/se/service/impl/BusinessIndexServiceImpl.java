package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.BusinessIndexDao;
import com.se.dao.base.BaseDao;
import com.se.entity.BusinessIndex;
import com.se.service.BusinessIndexService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class BusinessIndexServiceImpl extends BaseServiceImpl<BusinessIndex> implements BusinessIndexService{
	@Resource
	private BusinessIndexDao businessindexDao;	
	public BaseDao<BusinessIndex> getBaseDao() {
		return this.businessindexDao;
	}
}
