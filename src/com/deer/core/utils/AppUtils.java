package com.deer.core.utils;

import java.io.File;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JsonConfig;

public class AppUtils {

	/*客户端请求失败响应的内容*/
	public static void error(PrintWriter writer){
		String flag = "{\"state\":\"error\"}";
		writer.write(flag);
	}
	
	/*客户端请求成功响应的内容*/
	public static void success(PrintWriter writer){
		String flag = "{\"state\":\"success\"}";
		writer.write(flag);
	}
	
	/*获取客户端请求头传过来的用户ID*/
	public static String getHeader(HttpServletRequest request,String header){
		String value = request.getHeader(header);
		return value;
	}
	
	/*过滤给客户端不必要的数据*/
	public static JsonConfig filterField(String[] excludes){
		JsonConfig jsonConfig = new JsonConfig(); 
		jsonConfig.setIgnoreDefaultExcludes(false);
		jsonConfig.setExcludes(excludes);
		return jsonConfig; 
	}
	
	/*删除本地保存的发布心愿的的图片*/
	public static void deleteFile(String filepath){
		File file = new File(filepath);
		if (file.exists()) {
			file.delete();
		}
	}
	
	/*获取项目的真实路径*/
	public static String getPath(String path,HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath(path);
	}

}
