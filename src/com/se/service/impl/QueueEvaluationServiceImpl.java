package com.se.service.impl;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.se.dao.QueueEvaluationDao;
import com.se.dao.base.BaseDao;
import com.se.entity.QueueEvaluation;
import com.se.service.QueueEvaluationService;
import com.se.service.base.impl.BaseServiceImpl;
@Service
public class QueueEvaluationServiceImpl extends BaseServiceImpl<QueueEvaluation> implements QueueEvaluationService{
	@Resource
	private QueueEvaluationDao queueevaluationDao;	
	public BaseDao<QueueEvaluation> getBaseDao() {
		return this.queueevaluationDao;
	}
}
