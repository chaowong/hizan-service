package com.se.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

public class BuaaSMS {

	// httppostURL地址，请根据实际修改地址
	//private static String httpUrl = "http://192.168.1.201:6224/Api/Execute";
	
	private static String httpUrl = "http://sms.buaa.edu.cn/Api/Execute";//北航短信接口地址
	private static String demoUserString = "ihome";
	private static String demoPasswString = "111111";
	private static String demoReceiveNum="18128905889";
	 
	private static String WebServiceUrl = "http://192.168.1.201:6224/Api.asmx";

	public static void main(String[] args) throws Exception {

		// post参数，其他方法请按文档传入参数
		String command = "<Root>"
				+ "<Function>SendMessage</Function>" // 标识发送短信接口
				+ "<LoginName>" + demoUserString + "</LoginName>" // 必填,用户帐号，用于标识本次接口调用的操作者帐号
				+ "<Password>" + demoPasswString + "</Password >" // 必填,用户密码，用于验证本次接口调用的操作者帐号
				+ "<SignedData></SignedData>" // 数据安全校验,传入空值
				+ "<RequestData>"
				+ "<Title>测试</Title>"
				+ "<MessageType>2</MessageType>"
				+ "<ReceiveNum>" + demoReceiveNum + "</ReceiveNum>"
				+ "<Content>测试短息发送</Content></RequestData>"
				+ "</Root>";

		System.out.println(command);
		String result="";
		
		int Method = 0;
		switch (Method) {
		case 0: //表示http post模式
			Map<String, String> map = new HashMap<String, String>();
			map.put("Command", command);
			result = doPost(httpUrl, map);
			break;
		case 1: //表示webservice提交模式
			//注，传入的参数为字符串，故做以下替换
			command=command.replace("<", "&lt;");
			command=command.replace(">", "&gt;");	
			result=doWebService(WebServiceUrl,command);
			break;
		}
		
		System.out.println("调用结果:"+result);

	}
	
	public static String sendMessage(String content, String receiveNum, int method) throws Exception {

		String url = "http://sms.buaa.edu.cn/Api/Execute";//北航短信接口地址
		String username = "ihome";
		String password = "111111";
		
		System.out.println(password);
		// post参数，其他方法请按文档传入参数
		String command = "<Root>" + "<Function>SendMessage</Function>" // 标识发送短信接口
				+ "<LoginName>" + username + "</LoginName>" // 必填,用户帐号，用于标识本次接口调用的操作者帐号
				+ "<Password>" + password + "</Password >" // 必填,用户密码，用于验证本次接口调用的操作者帐号
				+ "<SignedData></SignedData>" // 数据安全校验,传入空值
				+ "<RequestData>" 
				+ "<Title>北航</Title>" 
				+ "<MessageType>2</MessageType>" 
				+ "<ReceiveNum>" + receiveNum + "</ReceiveNum>" 
				+ "<Content>" + content + "</Content>"
				+ "</RequestData>" 
				+ "</Root>";
		System.out.println(command);
		String result = "";
		switch (method) {
		case 0: // 表示http post模式
			Map<String, String> map = new HashMap<String, String>();
			map.put("Command", command);
			result = doPost(url, map);
			break;
		case 1: // 表示webservice提交模式
			// 注，传入的参数为字符串，故做以下替换
			command = command.replace("<", "&lt;");
			command = command.replace(">", "&gt;");
			result = doWebService(WebServiceUrl, command);
			break;
		}
		return result;
	}

	// post请求方式
	private static String doPost(String url, Map<String, String> params) {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				sb.append(e.getKey());
				sb.append("=");
				sb.append(e.getValue());
				sb.append("&");
			}
			sb.substring(0, sb.length() - 1);
		}
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			OutputStreamWriter osw = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}


    //webservice模式，如果编译报错，请引用lib下的jar包
	private static String doWebService(String url,String command) throws HttpException, IOException
	{
		String soapResponseData="";
		
		String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
				+ " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
				+ " xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
				+ " <soap12:Body>"
				+ " <Execute xmlns=\"http://tempuri.org/\"><Command>"
				+ command + "</Command></Execute>" + "</soap12:Body>"
				+ " </soap12:Envelope>";

		PostMethod postMethod = new PostMethod(WebServiceUrl);
	
		// 然后把Soap请求数据添加到PostMethod中
		byte[] b = soapRequestData.getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(b, 0, b.length);
		RequestEntity re = new InputStreamRequestEntity(is, b.length,
				"application/soap+xml; charset=utf-8");
		postMethod.setRequestEntity(re);

		// 最后生成一个HttpClient对象，并发出postMethod请求
		HttpClient httpClient = new HttpClient();
		int statusCode = httpClient.executeMethod(postMethod);
		if (statusCode == 200) {
		    soapResponseData = postMethod.getResponseBodyAsString();
		} else {
			System.out.println("调用失败！错误码：" + statusCode);
		}			
		return soapResponseData;
	}
	
	
}
