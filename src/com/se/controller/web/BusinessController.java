package com.se.controller.web;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.JsonUtil;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.trace.Trace;
import com.se.entity.Business;
import com.se.service.BusinessQueueService;
import com.se.service.BusinessService;

@Controller
public class BusinessController {
	@Resource
	private BusinessService businessService;

	@Resource
	private BusinessQueueService businessqueueService;

	
	/* *** 获取业务列表 *** */
	@RequestMapping("app/business/listAll")
	public void listAll(HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> stateMap=new HashMap<String, String>();
			stateMap.put("state", "1");
			List<Business> items = this.businessService.listBy(stateMap);
			
			for(int i=0;i<items.size();i++){
				//该业务的当前等待用户：（1=已取号在排队）
				Map<String,String> businessMap= new HashMap<String,String>();
				businessMap.put("businessid", items.get(i).getId());
				businessMap.put("state", "1");
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
	
	/* *** 模糊查询推荐的业务列表 *** */
	@RequestMapping("app/business/listByDescription")
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

}
