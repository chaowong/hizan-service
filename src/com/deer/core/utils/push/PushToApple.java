package com.deer.core.utils.push;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.deer.core.utils.Parameter;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;

public class PushToApple {
	
	/**
	 * 对单个用户进行推送
	 * @param message	推送内容
	 * @param devicetoken  客服唯一标示
	 * @param state		透传消息内容
	 * @throws Exception
	 */
	public static void pushToIos( String devicetoken,String message, String state) throws Exception {
		IGtPush push = new IGtPush(Parameter.url, Parameter.appkey, Parameter.masterSecret);
		APNTemplate t = new APNTemplate();
		t.setPushInfo("", 1, message, "default", state, "", "", "");
		SingleMessage sm = new SingleMessage();
		sm.setData(t);
		IPushResult ret0 = push.pushAPNMessageToSingle(Parameter.appid, devicetoken, sm);
		// System.out.println(ret0.getResponse().toString());
	}

	/**
	 * 向群体用户推送
	 * @throws Exception
	 */
	public static Map<String, Object> pushToIosList(String content,List<String> tokens) throws Exception {
		IGtPush push = new IGtPush(Parameter.url, Parameter.appkey, Parameter.masterSecret);
		APNTemplate t = new APNTemplate();
		/**
		 * setPushInfo(actionLocKey, badge, message, sound, payload, locKey, locArgs, launchImage)
		 * actionLocKey：	（用于多语言支持）指定执行按钮所使用的Localizable.strings
		 * badge：	应用icon上显示的数字
		 * message：	通知文本消息字符串
		 * sound：	通知铃声文件名（无声设置：com.gexin.ios.silence）
		 * payload：	需要发送给客户端的透传数据
		 * locKey：	（用于多语言支持）指定Localizable.strings文件中相应的key
		 * locArgs：	如果loc-key中使用的占位符，则在loc-args中指定各参数
		 * launchImage：	指定启动界面图片名
		 * ContentAvailable:	推送直接带有透传数据
		 * category:	在客户端通知栏触发特定的action和button显示
		*/
		t.setPushInfo("ok", 3,content, "default",content, "", "", "");
		ListMessage message = new ListMessage();
		message.setData(t);
		String contentId = push.getAPNContentId(Parameter.appid, message);
		System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		IPushResult ret = push.pushAPNMessageToList(Parameter.appid, contentId, tokens);
		return ret.getResponse();
	}
	/**
	 * 向群体用户推送
	 * @throws Exception
	 */
	@Test
	public void pushToIosListTest() throws Exception {
		IGtPush push = new IGtPush(Parameter.url, Parameter.appkey, Parameter.masterSecret);
		APNTemplate t = new APNTemplate();
		/**
		 * setPushInfo(actionLocKey, badge, message, sound, payload, locKey, locArgs, launchImage)
		 * actionLocKey：	（用于多语言支持）指定执行按钮所使用的Localizable.strings
		 * badge：	应用icon上显示的数字
		 * message：	通知文本消息字符串
		 * sound：	通知铃声文件名（无声设置：com.gexin.ios.silence）
		 * payload：	需要发送给客户端的透传数据
		 * locKey：	（用于多语言支持）指定Localizable.strings文件中相应的key
		 * locArgs：	如果loc-key中使用的占位符，则在loc-args中指定各参数
		 * launchImage：	指定启动界面图片名
		 * ContentAvailable:	推送直接带有透传数据
		 * category:	在客户端通知栏触发特定的action和button显示
		 */
		t.setPushInfo("ok", 1,"消息内容...", "default","透传数据内容", "", "", "");
		ListMessage message = new ListMessage();
		message.setData(t);
		String contentId = push.getAPNContentId(Parameter.appid, message);
		List<String> dtl = new ArrayList<String>();
		String devicetoken="993cf3ac9c819d32b87f2458dbda71a11c17c3f1b64b7062bedd1a387295fa30";
		//String devicetoken2="b405b68961296321cb5876443c72b176ab74562dc7866989a7e1ef9f7815d3ef";
		dtl.add(devicetoken);
		//dtl.add(devicetoken2);
		System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		IPushResult ret = push.pushAPNMessageToList(Parameter.appid, contentId, dtl);
		System.out.println(ret.getResponse());
		System.out.println(ret.getResponse().get("result"));
	}
	/**
	 * 对单个用户进行推送
	 * @param message	推送内容
	 * @param devicetoken  客服唯一标示
	 * @param state		透传消息内容
	 * @throws Exception
	 */
	@Test
	public void pushToIos() throws Exception {
		IGtPush push = new IGtPush(Parameter.url, Parameter.appkey, Parameter.masterSecret);
		APNTemplate t = new APNTemplate();
		t.setPushInfo("", 1, "文本消息", "default", "透传消息", "", "", "");
		SingleMessage sm = new SingleMessage();
		sm.setData(t);
		String devicetoken="993cf3ac9c819d32b87f2458dbda71a11c17c3f1b64b7062bedd1a387295fa30";
		IPushResult ret0 = push.pushAPNMessageToSingle(Parameter.appid, devicetoken, sm);
		System.out.println(ret0.getResponse().toString());
	}

}
