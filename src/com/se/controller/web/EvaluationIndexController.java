package com.se.controller.web;

import java.io.PrintWriter;
import java.util.List;

import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.JsonUtil;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.trace.Trace;
import com.se.entity.Business;
import com.se.entity.EvaluationIndex;
import com.se.service.EvaluationIndexService;

@Controller
public class EvaluationIndexController {
	@Resource
	private EvaluationIndexService evaluationindexService;

	/* *** 获取评价指标列表 *** */
	@RequestMapping("app/index/listAll")
	public void listAll(HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			List<EvaluationIndex> items = this.evaluationindexService.listAll();
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer,Parameter.SUCCESS, Parameter.WIN, content);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer,Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

}
