package com.deer.core.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 常用参数类
 * @author WangChengBin
 *
 */
@SuppressWarnings("all")
public class Parameter {

	public static final Map<String, Object> token = new HashMap<String, Object>();//客户端操作口令
	
	public static final String SUCCESS="success";   //客户端操作成功标示
	public static final String DEFEAT="defeat";		//客户端操作失败标示
	
	public static final String WIN          = "200";//操作成功
	public static final String ERROR        = "500";//操作错误
	public static final String FAILURE      = "400";//操作失败
	public static final String TIMEOUT      = "401";//请求超时
	
	public static final String EXIST        = "403";//记录已存在
	public static final String NOTFOUND     = "404";//查不到记录
	public static final String SIGN         = "402";//签名错误
	public static final String UNREASONABLE = "405";//非法操作
	
	
	/* ***个推配置 *** */
	public static String appid = "dqL7zCmc4d8GEltrMiqqe1";
	public static String appkey = "IK31XAhMWu8w9KoXwd4vj9";
	public static String masterSecret = "2AC7ziNvNu7zToAYvRWdd6";
	public static String url ="http://sdk.open.api.igexin.com/serviceex";
	/* *** END *** */
	
	/* ***短信配置 *** */
	
	
}
