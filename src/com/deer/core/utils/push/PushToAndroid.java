package com.deer.core.utils.push;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.deer.core.utils.Parameter;
import com.deer.core.utils.trace.Trace;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class PushToAndroid {
	 
	/**
	 * 对单个用户推送
	 * @param clientid	客户唯一标示
	 * @param state	透传消息内容
	 * @throws Exception
	 */
	public static void pushToAndroid(String token,String content,String state) throws Exception {
		IGtPush push = new IGtPush(Parameter.url, Parameter.appkey, Parameter.masterSecret);
		String cid = token;
		try {
			push.connect();
			NotificationTemplate template = NotificationTemplateDemo(content,state);
			//TransmissionTemplate template = transmissionTemplateDemo(content);
			SingleMessage mess = new SingleMessage();
			mess.setData(template);
			mess.setOffline(true);
			mess.setOfflineExpireTime(1 * 1000 * 3600);
			Target target1 = new Target();
			target1.setAppId(Parameter.appid);
			target1.setClientId(cid);
			IPushResult ret = push.pushMessageToSingle(mess, target1);
			System.out.println(ret.getResponse().toString());
		} catch (Exception e) {
			Trace.printStackTrace(e);
		}
	}
	/**
	 * 向群体用户推送
	 * @param content  透传消息内容
	 * @param tokens   群体用户token
	 * @throws IOException
	 */
	public static void pushToAndroidList(String content,List<String> tokens) throws IOException{
		IGtPush push = new IGtPush(Parameter.url, Parameter.appkey, Parameter.masterSecret);
        //建立连接，开始鉴权
        push.connect();
        //通知透传模板
        //NotificationTemplate template = notificationTemplateDemo();
        TransmissionTemplate template = transmissionTemplateDemo(content);
        ListMessage message = new ListMessage();
        message.setData(template);
        //设置消息离线，并设置离线时间
        message.setOffline(true);
        //离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24*1000*3600);
        //配置推送目标
        List<Target> targets = new ArrayList<Target>();
        for (String token : tokens) {
        	Target target = new Target();
        	target.setAppId(Parameter.appid);
        	target.setClientId(token);
        	targets.add(target);
		}
        //获取taskID
        String taskId = push.getContentId(message);
        //使用taskID对目标进行推送
        IPushResult ret = push.pushMessageToList(taskId, targets);
        //打印服务器返回信息
        System.out.println(ret.getResponse().toString());
	}
	
	/*点击通知打开应用模板*/
	public static NotificationTemplate NotificationTemplateDemo(String message,String state)
			throws Exception {
		NotificationTemplate template = new NotificationTemplate();
		template.setAppId(Parameter.appid);
	    template.setAppkey(Parameter.appkey);
	    template.setTitle("Favor"); // 设置通知栏标题与内容
	    template.setText(message); // 设置文本内容
//	    template.setLogo("icon.png"); // 配置通知栏图标
//	    template.setLogoUrl(""); // 配置通知栏网络图标
	    template.setIsRing(true); // 设置通知是否响铃，震动，或者可清除
	    template.setIsVibrate(true);
	    template.setIsClearable(true);
	    template.setTransmissionType(2); // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionContent(state);//透传内容
	    return template;
	}
	
	/*透传模版*/
	public static TransmissionTemplate transmissionTemplateDemo(String content) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(Parameter.appid);
	    template.setAppkey(Parameter.appkey);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(2);
	    template.setTransmissionContent(content);//请输入需要透传的内容
	    return template;
	}
	
	/**
	 * 测试群体用户推送
	 */
	@Test
	public void listtest(){
		String content ="透传消息";
		List<String> tokens = new ArrayList<String>();
		tokens.add("fce4bb09b0b26189083b68e5bf8351e9");
		try {
			pushToAndroidList(content, tokens);
		} catch (IOException e) {
			Trace.printStackTrace(e);
		}
	}
	
	@Test
	public void test(){
		try {
			String token="823311cbba02be815a16c63592fb9bcb";
			String content="{\"msg\":\"透传消息\"}";
			pushToAndroid(token, content,"");
		} catch (Exception e) {
			Trace.printStackTrace(e);
		}
	}
}
