package com.se.controller.web;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.JsonUtil;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.SendSMS;
import com.deer.core.utils.trace.Trace;
import com.se.entity.User;
import com.se.service.UserService;

@Controller
public class UserController {
	@Resource
	private UserService userService;

	/*
	 * @RequestMapping("app/user/login") public void login(final User user,
	 * HttpServletResponse response) {
	 * response.setContentType("text/html;charset=UTF-8"); User currentUser =
	 * userService.findByUser(user); PrintWriter writer =
	 * AppIoUtil.getWriter(response); if (currentUser != null) { //
	 * 判断手机及号码是否匹配，如果匹配则登录成功： if
	 * (user.getPassword().equals(currentUser.getPassword())) {
	 * user.setId(currentUser.getId()); String token = AppIoUtil.getUID();//
	 * step1随机生成一个token,待加密 System.out.println(token); byte[] key; try { String
	 * encrykey = currentUser.getId();// step2以uid为密钥 key =
	 * EncrypAES.initkey(encrykey);
	 * 
	 * System.out.print("密钥：");
	 * 
	 * for (int i = 0; i < key.length; i++) { System.out.printf("%x", key[i]); }
	 * byte[] encrypt = EncrypAES.encrypt(token.getBytes(), key); token = new
	 * BASE64Encoder().encode(encrypt); System.out.println("加密过后的token:" +
	 * token);
	 * 
	 * byte[] basedecode = new BASE64Decoder().decodeBuffer(token); byte[]
	 * decrypt = EncrypAES.decrypt(basedecode, key);
	 * 
	 * System.out.println("token解密：" + new String(decrypt)); } catch (Exception
	 * e) { Trace.printStackTrace(e); } userService.modify(user); String[] excludes =
	 * new String[] { "mobile", "location", "idcard", "email", "qq",
	 * "sinaweibo", "registertime", "realname", "wechat", "level", "vip",
	 * "flist", "plist" }; JsonConfig jsonConfig =
	 * AppUtils.filterField(excludes); JSONObject object =
	 * JSONObject.fromObject(currentUser, jsonConfig);
	 * System.out.println("给客户端的token：" + token); writer.write("{\"token\":\"" +
	 * token + "\",\"user\":" + object + "}"); // 推送离线消息
	 * 
	 * Map<String,Object> map= new HashMap<String, Object>(); map.put("userid",
	 * u.getId()); final List<News> news = this.newsService.selectAllmap(map);
	 * new Thread(new Runnable() { public void run() { for (final News n : news)
	 * { pushphone(user, n.getMode()+n.getContent(),n.getUsername()); } }
	 * }).start(); this.newsService.deleteById(u.getId());
	 * 
	 * AppIoUtil.closeSource(writer); } else { AppIoUtil.state(writer,
	 * Parameter.DEFEAT, Parameter.ERROR); } } else { AppIoUtil.state(writer,
	 * Parameter.DEFEAT, Parameter.NOTFOUND); } }
	 */

	/* 新用户注册 */
	@RequestMapping("app/user/register")
	public void reg(final User user, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			String userid = UUID.randomUUID().toString();
			user.setId(userid);
			userService.add(user);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* 通过第三方平台注册新用户  */
	@RequestMapping("app/user/thirdregister")
	public void thirdRegister(final User user, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			User u = userService.findBy(user);
			String[] excludes = new String[] { "idcard", "email" };
			JsonConfig config = AppUtils.filterField(excludes);
			String json;
			if (u == null) {
				user.setId(UUID.randomUUID().toString());
				userService.add(user);
				json = JsonUtil.object2json(user, config);
			}else{
				json = JsonUtil.object2json(u, config);
			}
			System.out.println("-----------------third---------------------");
			System.out.println(json);
			System.out.println("--------------------------------------");
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN,json);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* 用户登录 */
	@RequestMapping("app/user/login")
	public void login(final User user, HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", user.getMobile());
			map.put("password", user.getPassword());
			User u = userService.findByMap(map);
			if (u != null) {
				u.setClientid(user.getClientid());
				u.setDevicetoken(user.getDevicetoken());
				u.setAppversion(user.getAppversion());
				userService.modify(u);
				String[] excludes = new String[] { "idcard", "email" };
				JsonConfig config = AppUtils.filterField(excludes);
				//String json = JsonUtil.object2json(u, config);
			System.out.println(u.getMobile());
				JSONObject jsonObject = JSONObject.fromObject(u);
				String json=jsonObject.toString();
				System.out.println("------------------mobile--------------------");
				System.out.println(json);
				System.out.println("--------------------------------------------");
				
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, json);
			} else {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.NOTFOUND);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* 发送验证码 */
	@RequestMapping("app/user/sendcode")
	public void sendcode(String mobile, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", mobile);
			User u = userService.findByMap(map);
			if (u != null) {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.EXIST);
			} else {
				long sendtime = System.currentTimeMillis();
				String code = SendSMS.getCode();
				HttpSession session = request.getSession();
				String sessionid = session.getId();
				session.setAttribute("code", code);
				session.setAttribute("sendtime", sendtime);
				//1-使用短信服务商（易迅）接口
				SendSMS.sendSMS(mobile, code, "");
				//2-使用北航短信服务接口
				//BuaaSMS.sendMessage("您的验证码为："+code+"，5分钟内有效。", mobile, 0);
				//-
				String content = "{\"msg\":\"" + sessionid + "\"}";
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* 校验验证码 */
	@RequestMapping("app/user/validcode")
	public void validcode(String code, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			HttpSession session = request.getSession();
			String sessioncode = (String) session.getAttribute("code");
			long sessiontime = (Long) session.getAttribute("sendtime");
			long nowtime = System.currentTimeMillis();
			long timegap = nowtime - sessiontime;
			if (code != null && sessioncode.equals(code) && timegap < 600000) {
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
			} else {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* 用户资料更新 */
	@RequestMapping("app/user/update")
	public void update(final User user, HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", user.getMobile());
			map.put("password", user.getPassword());
			User u = userService.findByMap(map);
			if (u != null) {
				String[] excludes = new String[] { "idcard", "email" };
				JsonConfig config = AppUtils.filterField(excludes);
				String json = JsonUtil.object2json(u, config);
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, json);
			} else {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.NOTFOUND);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	
	/* 找回密码-1：发送验证码 */
	@RequestMapping("app/user/sendcode4reset")
	public void sendcode4reset(String mobile, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", mobile);
			User u = userService.findByMap(map);
			if (u != null) {
				
				long sendtime = System.currentTimeMillis();
				String code = SendSMS.getCode();
				HttpSession session = request.getSession();
				String sessionid = session.getId();
				session.setAttribute("code", code);
				session.setAttribute("sendtime", sendtime);
				//1-使用短信服务商（易迅）接口
				SendSMS.sendSMS(mobile, code, "");
				//2-使用北航短信服务接口
				//BuaaSMS.sendMessage("您的验证码为："+code+"，5分钟内有效。", mobile, 0);
				//-
				String content = "{\"msg\":\"" + sessionid + "\"}";
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
			} else {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.NOTFOUND);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* 找回密码-2：校验验证码 */
	@RequestMapping("app/user/validcode4reset")
	public void validcode4reset(String code, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			HttpSession session = request.getSession();
			String sessioncode = (String) session.getAttribute("code");
			long sessiontime = (Long) session.getAttribute("sendtime");
			long nowtime = System.currentTimeMillis();
			long timegap = nowtime - sessiontime;
			if (code != null && sessioncode.equals(code) && timegap < 600000) {
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
			} else {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	/* 找回密码-3：通过手机号码更新用户资料 */
	@RequestMapping("app/user/updateByMobile")
	public void updateByMobile(final User user, HttpServletResponse response) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("mobile", user.getMobile());
			User u = userService.findByMap(map);
			if (u != null) {
				//通过mobile查找到用户，将该用户的密码设置为参数用户user的密码
				u.setPassword(user.getPassword());
				userService.modify(u);
				String[] excludes = new String[] { "idcard", "email" };
				JsonConfig config = AppUtils.filterField(excludes);
				String json = JsonUtil.object2json(u, config);
				AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, json);
			} else {
				AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.NOTFOUND);
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}
	
}
