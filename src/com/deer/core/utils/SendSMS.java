package com.deer.core.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;



public class SendSMS {
	/*
	 * 发送方法  其他方法同理      返回0 或者 1 都是  提交成功
	 */
	/**
	 * @param Mobile   手机号码
	 * @param Code     验证码
	 * @param sendTime 发送时间
	 * @return
	 * @throws MalformedURLException
	 * @throws UnsupportedEncodingException
	 */
	public static int sendSMS(String Mobile,String Code,String sendTime) throws MalformedURLException, UnsupportedEncodingException {
		URL url = null;
		String CorpID="YXS02943";//账户名
		String Pwd="888888";//密码
		String content = "您的验证码为："+Code+"，5分钟内有效。";
		String sendContent=URLEncoder.encode(content.replaceAll("<br/>", " "), "GBK");//发送内容
		url = new URL("http://www.106551.com:81/ws/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+sendContent+"&Cell=&SendTime="+sendTime);
		
		/*String CorpID="YX03310";//账户名
		String Pwd="105566";//密码
		String content = "恭喜您获得"+money+"元京东电子劵,卡号："+code+",密码："+pwd+"。";
		String send_content=URLEncoder.encode(content.replaceAll("<br/>", " "), "GBK");//发送内容
		url = new URL("http://www.106551.com:81/ws/BatchSend.aspx?CorpID="+CorpID+"&Pwd="+Pwd+"&Mobile="+Mobile+"&Content="+send_content+"&Cell=&SendTime="+send_time);
		*/
		BufferedReader in = null; 
		int inputLine = 0;
		try {
//			System.out.println("开始发送短信手机号码为 ："+Mobile);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			inputLine = new Integer(in.readLine()).intValue();
			in.close();
		} catch (Exception e) {
//			System.out.println("网络异常,发送短信失败！");
			inputLine=-2;
		}
		
		//System.out.println("结束发送短信返回值：  "+inputLine);
		return inputLine;
	}
	
	/**
	 * 发送过后状态
	 * @param sendSMS
	 * @param writer
	 * @return
	 */
	public static int sendState(int sendSMS,PrintWriter writer,String sessionid){
		if (sendSMS==0||sendSMS==1) {
			writer.write("{\"flag\":\"success\",\"code\":\"200\",\"jsessionid\":\""+sessionid+"\"}");
			return 1;
		}else if (sendSMS==-4) {
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);//手机号码格式错误
		}else {
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.UNREASONABLE);//其它非法 操作
		}
		return 0;
	}
	/**
	 * 获取验证码
	 * @return
	 */
	public static String getCode(){
		Random r = new Random();
		String code = "";
		for (int i = 0; i < 6; i++) {
			code+=r.nextInt(10);
		}
		return code;
	}
	
	public static void main(String[] args) throws Exception{
		String code = SendSMS.getCode();
		System.out.println(code);
		System.out.println(code);
		SendSMS.sendSMS("18128905889", code, "");
	}
}
