package com.deer.core.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.deer.core.utils.trace.Trace;

public class AppIoUtil {

	/**
	 * 获取writer
	 * @param response
	 * @return
	 */
	public static PrintWriter getWriter(HttpServletResponse response){
		PrintWriter writer=null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			Trace.printStackTrace(e);
		}
		return writer;
	}
	/**
	 * 返回数据
	 * @param writer
	 */
	public static void state(PrintWriter writer,String state,String code,String content){
		
		String result = "{\"state\":\""+state+"\",\"code\":\""+code+"\",\"content\":"+content+"}";
		writer.write(result);
	}
	/**
	 * 返回状态
	 * @param writer
	 */
	public static void state(PrintWriter writer,String state,String code){
		
		String result = "{\"state\":\""+state+"\",\"code\":\""+code+"\"}";
		writer.write(result);
	}
	/**
	 * 关闭流
	 * @param writer
	 */
	public static void closeSource(PrintWriter writer){
		if (writer!=null) {
			writer.close();
		}
	}
	
	public static final int MAX = Integer.MAX_VALUE;
	
	public static final int MIN = (int) MAX/2;
	
	/*生成注册用户的ID值*/
	public static String getUID(){
		String uuid = ((MIN + Math.random() * (MAX - MIN))+"").replace(".", "");
		return uuid.replace("E", "e");
	}
}
