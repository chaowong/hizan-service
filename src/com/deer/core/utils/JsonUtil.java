package com.deer.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.xml.XMLSerializer;

/**
 * json格式转换
 * @author WangChengBin
 *
 */
@SuppressWarnings("all")
public class JsonUtil {
	/**
	 * 将数组转换成String类型的JSON数据格式
	 * 
	 * @param objects
	 * @return
	 */
	public static String array2json(Object[] objects){
		
		JSONArray jsonArray = JSONArray.fromObject(objects);
		return jsonArray.toString();
		
	}
	
	/**
	 * 将list集合转换成String类型的JSON数据格式
	 * 
	 * @param list
	 * @return
	 */
	public static String list2json(List list){
		
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray.toString();
		
	}
	/**
	 * 将list集合转换成符合条件的String类型的JSON数据格式
	 * 
	 * @param list
	 * @return
	 */
	public static String list2json(List list,JsonConfig config){
		
		JSONArray jsonArray = JSONArray.fromObject(list, config);
		return jsonArray.toString();
		
	}
	
	/**
	 * 将map集合转换成String类型的JSON数据格式
	 * 
	 * @param map
	 * @return
	 */
	public static String map2json(Map map){
		
		JSONObject jsonObject = JSONObject.fromObject(map);
		return jsonObject.toString();
		
	}
	
	/**
	 * 将Object对象转换成String类型的JSON数据格式
	 * 
	 * @param object
	 * @return
	 */
	public static String object2json(Object object){
		
		JSONObject jsonObject = JSONObject.fromObject(object);
		return jsonObject.toString();
		
	}
	/**
	 * 将Object对象转换成符合条件的String类型的JSON数据格式
	 * 
	 * @param object
	 * @return
	 */
	public static String object2json(Object object,JsonConfig config){
		
		JSONObject jsonObject = JSONObject.fromObject(object,config);
		return jsonObject.toString();
		
	}
	
	/**
	 * 将XML数据格式转换成String类型的JSON数据格式
	 * 
	 * @param xml
	 * @return
	 */
	public static String xml2json(String xml){
		
		JSONArray jsonArray = (JSONArray) new XMLSerializer().read(xml);
		return jsonArray.toString();
		
	}
	
	/**
	  * 除去不想生成的字段（特别适合去掉级联的对象）
	  *
	  * @param excludes
	  * @return
	*/
	public static JsonConfig configJson(String[] excludes) {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);
		jsonConfig.setIgnoreDefaultExcludes(true);
		jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		return jsonConfig;
	}
	
	/** 过滤指定类的属性
	 * @param clazz
	 * @param excudes
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static JsonConfig config(String clazz,String[] excudes) throws ClassNotFoundException{
		JsonConfig config = new JsonConfig();
		config.registerPropertyExclusions(Class.forName(clazz), excudes);
		config.setAllowNonStringKeys(true);
		return config;
	}
}
