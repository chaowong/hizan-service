package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.AdviceDao;
import com.se.dao.base.BaseDao;
import com.se.entity.Advice;
import com.se.service.AdviceService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class AdviceServiceImpl extends BaseServiceImpl<Advice> implements AdviceService{
	@Resource
	private AdviceDao adviceDao;	
	public BaseDao<Advice> getBaseDao() {
		return this.adviceDao;
	}
}
