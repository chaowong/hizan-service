package com.se.controller.web;

import java.io.PrintWriter;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.trace.Trace;
import com.se.entity.BusinessQueue;
import com.se.entity.QueueEvaluation;
import com.se.service.BusinessQueueService;
import com.se.service.QueueEvaluationService;

@Controller
public class QueueEvaluationController {
	@Resource
	private QueueEvaluationService queueevaluationService;
	@Resource
	private BusinessQueueService queueService;
	@SuppressWarnings("all")
	@Test
	public void test(){
		String json = "[{\"indexid\":\"1\",\"score\":\"1\"},{\"indexid\":\"2\",\"score\":\"2\"},{\"indexid\":\"3\",\"score\":\"3\"},{\"content\":\"123\"}]";
		JSONArray array = new JSONArray().fromObject(json);
		for (Object object : array) {
			@SuppressWarnings("unused")
			JSONObject obj = new JSONObject();
			JSONObject fromObject = JSONObject.fromObject(object);
			QueueEvaluation eval = (QueueEvaluation) fromObject.toBean(fromObject,QueueEvaluation.class);
			System.out.println(eval.getIndexid());
		}
	}

	/* *** 添加业务评价信息，通过json字符串的形式传递数据 *** */
	@RequestMapping("app/evaluation/addOne")
	public void addOne(String evaluationsJson, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentUser = AppUtils.getHeader(request, "curuid");
		String queueid="";
		try {
			JSONArray array = JSONArray.fromObject(evaluationsJson);
			for (Object object : array) {
				JSONObject fromObject = JSONObject.fromObject(object);
				QueueEvaluation evaluation = (QueueEvaluation) JSONObject.toBean(fromObject,QueueEvaluation.class);
				evaluation.setId(UUID.randomUUID().toString());
				evaluation.setCreatedby(currentUser);
				evaluation.setCreatedon(System.currentTimeMillis());
				queueid=evaluation.getQueueid();
				queueevaluationService.add(evaluation);
				
			}
			//--评价完毕需要更新状态
			BusinessQueue queue = queueService.findById(queueid);
			queue.setState("4");
			queueService.modify(queue);
			AppIoUtil.state(writer,Parameter.SUCCESS, Parameter.WIN);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer,Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

}
