package com.se.controller.console;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JsonConfig;

import org.apache.commons.collections.map.HashedMap;
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
public class EvaluationIndexConsoleController {
	@Resource
	private EvaluationIndexService evaluationindexService;

	/* *** 获取评价指标列表 *** */
	@RequestMapping("console/index/listAll")
	public void listAll(HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			List<EvaluationIndex> items = this.evaluationindexService.listAll();
			/*Map<String, String> map= new HashMap<String, String>();
			for (int i = 0; i < items.size(); i++) {
				map.put("id", items.get(i).getId());
				map.put("text", items.get(i).getTitle());
			}*/
			String content = JsonUtil.list2json(items);
			AppIoUtil.state(writer,Parameter.SUCCESS, Parameter.WIN, content);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer,Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

}
