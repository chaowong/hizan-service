package com.se.controller.web;

import java.io.PrintWriter;
import java.util.UUID;

import org.junit.Test;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.STEditAs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.trace.Trace;
import com.se.entity.Advice;
import com.se.entity.User;
import com.se.service.AdviceService;
@Controller
public class AdviceController {
         @Resource
         private AdviceService adviceService;

     	/* 用户提交建议 */
     	@RequestMapping("app/advice/add")
     	public void addAdvice(String content, HttpServletResponse response, HttpServletRequest request) {
     		PrintWriter writer = AppIoUtil.getWriter(response);
     		String currentUser = AppUtils.getHeader(request, "curuid");
     		try {
     			Advice advice=new Advice();
     			String id = UUID.randomUUID().toString();
     			long sendtime = System.currentTimeMillis();
     			advice.setId(id);
     			advice.setContent(content);
     			advice.setCreatedby(currentUser);
     			advice.setCreatedon(sendtime);
     			adviceService.add(advice);
     			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
     		} catch (Exception e) {
     			Trace.printStackTrace(e);
     			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
     		} finally {
     			AppIoUtil.closeSource(writer);
     		}
     	}
     	 
}
