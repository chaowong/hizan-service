package com.se.controller.console;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.JsonUtil;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.push.PushToAndroid;
import com.deer.core.utils.trace.Trace;
import com.se.entity.Business;
import com.se.entity.BusinessQueue;
import com.se.entity.User;
import com.se.service.BusinessIndexService;
import com.se.service.BusinessQueueService;
import com.se.service.BusinessService;
import com.se.service.EvaluationIndexService;

@Controller
public class BusinessConsoleController {
	@Resource
	private BusinessService businessService;

	@Resource
	private BusinessQueueService businessqueueService;

	@Resource
	private EvaluationIndexService evaluationIndexService;

	
	/* *** 获取业务列表 *** */
	@RequestMapping("console/business/listAll")
	public void listAll(HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			List<Business> items = this.businessService.listAll();
			for(int i=0;i<items.size();i++){
				//该业务的当前等待用户：（1=已取号在排队）
				Map<String,String> businessMap= new HashMap<String,String>();
				businessMap.put("businessid", items.get(i).getId());
				businessMap.put("state", "1");
				items.get(i).setQueueCounter(businessqueueService.countBy(businessMap));
				items.get(i).setIndexList(evaluationIndexService.listAll());
				
			}
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer,Parameter.SUCCESS, Parameter.WIN,content);
			AppIoUtil.closeSource(writer);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer,Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	
	/* *** 模糊查询推荐的业务列表 *** */
	@RequestMapping("console/business/listByDescription")
	public void listByDescription(String description,HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String,String> descriptionMap= new HashMap<String,String>();
			descriptionMap.put("description",description);
			List<Business> items = this.businessService.listByDescription(descriptionMap);
			for(int i=0;i<items.size();i++){
				//该业务的当前等待用户：1=已取号
				Map<String,String> businessMap= new HashMap<String,String>();
				businessMap.put("businessid", items.get(i).getId());
				businessMap.put("state", "1");//1=已取号
				items.get(i).setQueueCounter(businessqueueService.countBy(businessMap));
			}
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer,Parameter.SUCCESS, Parameter.WIN,content);
			AppIoUtil.closeSource(writer);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer,Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	
	/* *** 添加用户取号队列信息synchronized *** */
	@RequestMapping("console/business/addOne")
	public synchronized void addOne(String id,String title,String description,String indexes,String windows,String state, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			if(id!=null&&!id.equals("")){
				Business entity=businessService.findById(id);
				entity.setTitle(title);
				entity.setDescription(description);
				entity.setState(state);
				businessService.modify(entity);
			}else{
				long now = System.currentTimeMillis();
				Business entity = new Business();
				entity.setId(UUID.randomUUID().toString());
				entity.setTitle(title);
				entity.setDescription(description);
				entity.setState(state);
				entity.setCreatedon(now);
				businessService.add(entity);
			}
			
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);

		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

}
