package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.EvaluationIndexDao;
import com.se.dao.base.BaseDao;
import com.se.entity.EvaluationIndex;
import com.se.service.EvaluationIndexService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class EvaluationIndexServiceImpl extends BaseServiceImpl<EvaluationIndex> implements EvaluationIndexService{
	@Resource
	private EvaluationIndexDao evaluationindexDao;	
	public BaseDao<EvaluationIndex> getBaseDao() {
		return this.evaluationindexDao;
	}
}
