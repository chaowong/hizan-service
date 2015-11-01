package com.se.controller.web;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.JsonUtil;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.trace.Trace;
import com.se.entity.Business;
import com.se.entity.BusinessEmployee;
import com.se.entity.BusinessQueue;
import com.se.entity.EvaluationIndex;
import com.se.service.BusinessEmployeeService;
import com.se.service.BusinessQueueService;
import com.se.service.BusinessService;

@Controller
public class BusinessEmployeeController {
	
	@Resource
	private BusinessService businessService;

	@Resource
	private BusinessQueueService businessqueueService;
	
	@Resource
	private BusinessEmployeeService businessemployeeService;

	@Resource
	private BusinessQueueService businessequeueService;
	
	/* *** 获取当前员工所负责的业务的取号队列 *** */
	@RequestMapping("app/employee/listByBusiness")
	public void listByBusiness(HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentUser = AppUtils.getHeader(request, "curuid");
		try {
			Map<String, String> userMap = new HashMap<String, String>();
			userMap.put("employeeid", currentUser);
			BusinessEmployee busiEmployee = businessemployeeService.findByMap(userMap);

			Map<String, String> busiMap = new HashMap<String, String>();
			String businessid = busiEmployee.getBusinessid();
			busiMap.put("businessid", businessid);
			List<BusinessQueue> items = businessequeueService.listByEmployeePerform(busiMap);
			Business business = businessService.findById(businessid);
			
			Map<String, Object> jsonMap =new HashMap<String, Object>();
			jsonMap.put("items", items);
			jsonMap.put("business", business);
			
			String[] excludes = new String[] { "indexList", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.object2json(jsonMap, jsonConfig);
			
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	/* *** 获取当前登录用户所负责业务的所有队列表 *** */
	@RequestMapping("app/employee/listByEmployee")
	public void listByEmployee(HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentEmployee = AppUtils.getHeader(request, "curuid");
		try {
			Map<String, String> emplyeeMap = new HashMap<String, String>();
			emplyeeMap.put("employeeid", currentEmployee);
			BusinessEmployee businessEmployee = businessemployeeService.findByMap(emplyeeMap);
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("businessid", businessEmployee.getBusinessid());
			map.put("state", "4");
			// 查询当前业务正【在排队】的列表
			List<BusinessQueue> items = this.businessqueueService.listBy(map);
			for (int i = 0; i < items.size(); i++) {
				Business business = businessService.findById(items.get(i).getBusinessid());
				// 查询当前业务正【在排队】的人数
				Map<String, String> businessMap = new HashMap<String, String>();
				businessMap.put("businessid", items.get(i).getBusinessid());
				businessMap.put("state", "4");// 1=已取号
				business.setQueueCounter(businessqueueService.countBy(businessMap));
				items.get(i).setBusiness(business);
			}
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
			AppIoUtil.closeSource(writer);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	
	/* *** 获取[今天][当前用户]所负责业务的所有队列表 *** */
	@RequestMapping("app/employee/listTodayByEmployee")
	public void listTodayByEmployee(HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentEmployee = AppUtils.getHeader(request, "curuid");
		try {
			Map<String, String> emplyeeMap = new HashMap<String, String>();
			emplyeeMap.put("employeeid", currentEmployee);
			BusinessEmployee businessEmployee = businessemployeeService.findByMap(emplyeeMap);
			
			Date today = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String fomatted_today = sdf.format(today);
			Date date1 = sdf.parse(fomatted_today);
			System.out.println(date1.getTime());  
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("businessid", businessEmployee.getBusinessid());
			map.put("createdon",String.valueOf(date1.getTime()));
			map.put("state", "4");
			// 查询当前业务正【在排队】的列表
			List<BusinessQueue> items = this.businessqueueService.listTodayBy(map);
			for (int i = 0; i < items.size(); i++) {
				Business business = businessService.findById(items.get(i).getBusinessid());
				// 查询当前业务正【在排队】的人数
				Map<String, String> businessMap = new HashMap<String, String>();
				businessMap.put("businessid", items.get(i).getBusinessid());
				businessMap.put("state", "4");// 1=已取号
				business.setQueueCounter(businessqueueService.countBy(businessMap));
				items.get(i).setBusiness(business);
			}
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
			AppIoUtil.closeSource(writer);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	
	public static void main(String args[]) throws ParseException{
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String fomatted_today = sdf.format(today);
		Date date1 = sdf.parse(fomatted_today);
		System.out.println(date1.getTime());   
		
	}
}
