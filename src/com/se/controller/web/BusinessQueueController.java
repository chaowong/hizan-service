package com.se.controller.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.deer.core.utils.AppIoUtil;
import com.deer.core.utils.AppUtils;
import com.deer.core.utils.Encryption;
import com.deer.core.utils.JsonUtil;
import com.deer.core.utils.Parameter;
import com.deer.core.utils.push.PushToAndroid;
import com.deer.core.utils.push.PushToApple;
import com.deer.core.utils.trace.Trace;
import com.se.entity.Business;
import com.se.entity.BusinessEmployee;
import com.se.entity.BusinessIndex;
import com.se.entity.BusinessQueue;
import com.se.entity.EvaluationIndex;
import com.se.entity.User;
import com.se.service.BusinessEmployeeService;
import com.se.service.BusinessIndexService;
import com.se.service.BusinessQueueService;
import com.se.service.BusinessService;
import com.se.service.EvaluationIndexService;
import com.se.service.UserService;

@Controller
public class BusinessQueueController {

	@Resource
	private BusinessQueueService businessqueueService;

	@Resource
	private BusinessService businessService;

	@Resource
	private BusinessIndexService businessIndexService;

	@Resource
	private EvaluationIndexService evaluationindexService;

	@Resource
	private UserService userService;

	@Resource
	private BusinessEmployeeService businessemployeeService;

	/* *** 添加用户取号队列信息synchronized *** */
	@RequestMapping("app/queue/addOne")
	public synchronized void addOne(String businessid, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentUser = AppUtils.getHeader(request, "curuid");
		try {

			if (currentUser == null || currentUser == "") {
				AppIoUtil.state(writer, Parameter.DEFEAT, "异常账号，请联系管理员！");
			} else {

				// -获取当前用户在该业务下已经预约的次数（即state=1）
				Map<String, String> myqueueMap = new HashMap<String, String>();
				myqueueMap.put("createdby", currentUser);
				myqueueMap.put("businessid", businessid);
				myqueueMap.put("state", "1");
				long myunclosedQueueCount = businessqueueService.countBy(myqueueMap);
				// -判断当前用户预约次数超过2次，则不允许继续预约
				if (myunclosedQueueCount > 2) {
					AppIoUtil.state(writer, Parameter.DEFEAT, "该业务您已取号3次，先取消再申请");
				} else {

					long now = System.currentTimeMillis();
					BusinessQueue entity = new BusinessQueue();
					entity.setId(UUID.randomUUID().toString());

					// - 1、统计今天已经产生的号数（不分业务类型），从而获得当前用户的队列号+1
					Map<String, Object> allbusinessMap = new HashMap<String, Object>();
					allbusinessMap.put("createdon", now);
					int curQueueNum = 1000 + businessqueueService.countFrontBy(allbusinessMap) + 1;
					Trace.println("此时此刻等待的人数(state=1):" + curQueueNum);

					// -
					// 2、统计今天当前业务已经产生的号数，从而获得当前业务排号的用户数即state=1的用户（不包括正在受理的用户数）
					Map<String, Object> businessMap = new HashMap<String, Object>();
					businessMap.put("businessid", businessid);
					businessMap.put("createdon", now);
					businessMap.put("state", "1");// 1=已取号
					int curBusinessQueueNum = businessqueueService.countFrontBy(businessMap) + 1;
					// 推送的内容：当前业务最新的state=1的用户数

					Map<String, String> touchuanJsonMap = new HashMap<String, String>();
					touchuanJsonMap.put("type", "1");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
					touchuanJsonMap.put("businessid", businessid);
					touchuanJsonMap.put("queuenum", "" + curBusinessQueueNum);// 排队人数

					final String content = JsonUtil.map2json(touchuanJsonMap);

					// 向所有用户开启更新排队人数的透传线程
					new Thread(new Runnable() {
						public void run() {
							List<String> clientids = new ArrayList<String>();
							List<User> userList = userService.listAll();
							for (User user : userList) {
								if (user.getClientid() != null && !user.getClientid().equals("")) {
									clientids.add(user.getClientid());
								}
							}
							try {
								PushToAndroid.pushToAndroidList(content, clientids);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						}
					}).start();
					entity.setQueuenum(curQueueNum);
					entity.setBusinessid(businessid);
					entity.setState("1");// 1=已取号，2=办理中，3=办理完，4=已评价，0=已取消，5=已过号
					entity.setCreatedby(currentUser);
					entity.setCreatedon(now);
					businessqueueService.add(entity);
					AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
				}
			}
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* *** 取消用户取号队列信息 *** */
	@RequestMapping("app/queue/cancelOne")
	public void cancelOne(String queueid, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			BusinessQueue entity = businessqueueService.findById(queueid);
			entity.setState("0");
			businessqueueService.modify(entity);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* *** 工作人员更新用户取号队列信息 *** */
	@RequestMapping("app/queue/updateOne")
	public void updateOne(String queueid, String state, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentEmployee = AppUtils.getHeader(request, "curuid");
		try {
			// - 获取老师所负责的窗口号，并写入订单的窗口中
			BusinessEmployee queryEmployee = new BusinessEmployee();
			queryEmployee.setEmployeeid(currentEmployee);
			BusinessEmployee businessEmployee = businessemployeeService.findBy(queryEmployee);
			String employeeWindow = businessEmployee.getWindow();
			// -
			BusinessQueue queue = businessqueueService.findById(queueid);
			String userid = queue.getCreatedby();
			final User user = userService.findById(userid);
			final String businessid = queue.getBusinessid();
			final String deviceToken = user.getDevicetoken();
			final String clientid = user.getClientid();
			// -当订单状态变为（2）受理中时：
			if (state.equalsIgnoreCase("2")) {

				Map<String, String> touchuanJsonMap = new HashMap<String, String>();
				touchuanJsonMap.put("type", "2");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
				touchuanJsonMap.put("queueid", queue.getId());
				touchuanJsonMap.put("state", "2");// 状态值变为2
				touchuanJsonMap.put("window", employeeWindow);// 受理该业务的窗口号
				touchuanJsonMap.put("showView", "true");// 用于安卓版的判断推送or透传，只有在推送时发送这个字段，表示时推送。
				final String content1 = JsonUtil.map2json(touchuanJsonMap);

				// -开启推送线程
				new Thread(new Runnable() {
					public void run() {
						String appversion = user.getAppversion();
						String appType = appversion.substring(0, appversion.indexOf("_"));

						if (appType.equalsIgnoreCase("ios")) {
							try {
								PushToApple.pushToIos(deviceToken, "开始受理您的业务，请即刻前往办理窗口。", content1);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						} else {
							try {
								PushToAndroid.pushToAndroid(clientid, "开始受理您的业务，请即刻前往办理窗口。", content1);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						}
					}
				}).start();
				// 开启当前用户状态更改的透传线程
				new Thread(new Runnable() {
					public void run() {
						List<String> clientids = new ArrayList<String>();
						clientids.add(clientid);
						try {
							PushToAndroid.pushToAndroidList(content1, clientids);
						} catch (Exception e) {
							Trace.printStackTrace(e);
						}
					}
				}).start();
				// 向所有用户开启更新排队人数的透传线程

				// - 2、统计今天当前业务已经产生的号数，从而获得当前业务排号的用户数即state=1的用户（不包括正在受理的用户数）
				long now = System.currentTimeMillis();
				Map<String, Object> queueMap = new HashMap<String, Object>();
				queueMap.put("businessid", businessid);
				queueMap.put("createdon", now);
				queueMap.put("state", "1");// 1=已取号
				int curBusinessQueueNum = businessqueueService.countFrontBy(queueMap) - 1;
				// 推送的内容：当前业务最新的state=1的用户数
				Trace.println("此时此刻等待的人数(state=1):" + curBusinessQueueNum);

				Map<String, String> touchuanJsonMap2 = new HashMap<String, String>();
				touchuanJsonMap2.put("type", "1");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
				touchuanJsonMap2.put("businessid", businessid);
				touchuanJsonMap2.put("queuenum", "" + curBusinessQueueNum);// 排队人数

				final String content = JsonUtil.map2json(touchuanJsonMap2);
				new Thread(new Runnable() {
					public void run() {
						List<String> clientids = new ArrayList<String>();
						List<User> userList = userService.listAll();
						for (User user : userList) {
							if (user.getClientid() != null && !user.getClientid().equals("")) {
								clientids.add(user.getClientid());
							}
						}
						try {
							PushToAndroid.pushToAndroidList(content, clientids);
						} catch (Exception e) {
							Trace.printStackTrace(e);
						}
					}
				}).start();

			}
			// 当订单状态变为（3）受理完时：
			if (state.equalsIgnoreCase("3")) {
				Map<String, String> touchuanJsonMap = new HashMap<String, String>();
				touchuanJsonMap.put("type", "2");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
				touchuanJsonMap.put("queueid", queue.getId());
				touchuanJsonMap.put("state", "3");// 状态值变为3
				touchuanJsonMap.put("window", employeeWindow);// 受理该业务的窗口号

				final String content2 = JsonUtil.map2json(touchuanJsonMap);
				// -开启推送线程
				new Thread(new Runnable() {
					public void run() {
						String appversion = user.getAppversion();
						String appType = appversion.substring(0, appversion.indexOf("_"));
						if (appType.equalsIgnoreCase("ios")) {
							try {
								PushToApple.pushToIos(deviceToken, "业务受理完毕，感谢您的信任。请对我们的服务进行评价。", content2);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						} else {
							try {
								PushToAndroid.pushToAndroid(clientid, "业务受理完毕，感谢您的信任。请对我们的服务进行评价。", content2);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						}
					}
				}).start();
				// 开启当前用户状态更改的透传线程
				new Thread(new Runnable() {
					public void run() {
						List<String> clientids = new ArrayList<String>();
						clientids.add(clientid);
						try {
							PushToAndroid.pushToAndroidList(content2, clientids);
						} catch (Exception e) {
							Trace.printStackTrace(e);
						}
					}
				}).start();
			}
			queue.setState(state);
			queue.setHandledby(currentEmployee);
			queue.setHandledon(System.currentTimeMillis());
			queue.setHandledwindow(employeeWindow);
			businessqueueService.modify(queue);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* *** 工作人员更新用户取号队列信息 *** */
	@RequestMapping("app/queue/updateOne2")
	public void updateOne2(String queueid, String state, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		String currentEmployee = AppUtils.getHeader(request, "curuid");
		try {
			// - 获取老师所负责的窗口号，并写入订单的窗口中
			BusinessEmployee queryEmployee = new BusinessEmployee();
			queryEmployee.setEmployeeid(currentEmployee);
			BusinessEmployee businessEmployee = businessemployeeService.findBy(queryEmployee);
			String employeeWindow = businessEmployee.getWindow();
			// -
			BusinessQueue queue = businessqueueService.findById(queueid);
			String userid = queue.getCreatedby();
			final User user = userService.findById(userid);
			final String businessid = queue.getBusinessid();
			final String deviceToken = user.getDevicetoken();
			final String clientid = user.getClientid();
			// -当订单状态变为（2）受理中时，透传+推送：
			if (state.equalsIgnoreCase("2")) {
				Map<String, String> touchuanJsonMap = new HashMap<String, String>();
				touchuanJsonMap.put("type", "2");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
				touchuanJsonMap.put("queueid", queue.getId());
				touchuanJsonMap.put("state", "2");// 状态值变为2
				touchuanJsonMap.put("window", employeeWindow);// 受理该业务的窗口号
				touchuanJsonMap.put("showView", "true");// 用于安卓版的判断推送or透传，只有在推送时发送这个字段，表示时推送。
				final String content1 = JsonUtil.map2json(touchuanJsonMap);

				// -开启推送线程
				new Thread(new Runnable() {
					public void run() {
						String appversion = user.getAppversion();
						String appType = appversion.substring(0, appversion.indexOf("_"));

						if (appType.equalsIgnoreCase("ios")) {
							try {
								PushToApple.pushToIos(deviceToken, "开始受理您的业务，请即刻前往办理窗口。", content1);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						} else {
							try {
								PushToAndroid.pushToAndroid(clientid, "开始受理您的业务，请即刻前往办理窗口。", content1);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						}
					}
				}).start();
				// 开启当前用户状态更改的透传线程
				new Thread(new Runnable() {
					public void run() {
						List<String> clientids = new ArrayList<String>();
						clientids.add(clientid);
						try {
							PushToAndroid.pushToAndroidList(content1, clientids);
						} catch (Exception e) {
							Trace.printStackTrace(e);
						}
					}
				}).start();
				// 向所有用户开启更新排队人数的透传线程

				// - 2、统计今天当前业务已经产生的号数，从而获得当前业务排号的用户数即state=1的用户（不包括正在受理的用户数）
				long now = System.currentTimeMillis();
				Map<String, Object> queueMap = new HashMap<String, Object>();
				queueMap.put("businessid", businessid);
				queueMap.put("createdon", now);
				queueMap.put("state", "1");// 1=已取号
				int curBusinessQueueNum = businessqueueService.countFrontBy(queueMap) - 1;
				// 推送的内容：当前业务最新的state=1的用户数
				Trace.println("此时此刻等待的人数(state=1):" + curBusinessQueueNum);

				Map<String, String> touchuanJsonMap2 = new HashMap<String, String>();
				touchuanJsonMap2.put("type", "1");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
				touchuanJsonMap2.put("businessid", businessid);
				touchuanJsonMap2.put("queuenum", "" + curBusinessQueueNum);// 排队人数

				final String content = JsonUtil.map2json(touchuanJsonMap2);
				new Thread(new Runnable() {
					public void run() {
						List<String> clientids = new ArrayList<String>();
						List<User> userList = userService.listAll();
						for (User user : userList) {
							if (user.getClientid() != null && !user.getClientid().equals("")) {
								clientids.add(user.getClientid());
							}
						}
						try {
							PushToAndroid.pushToAndroidList(content, clientids);
						} catch (Exception e) {
							Trace.printStackTrace(e);
						}
					}
				}).start();

			}
			// 当订单状态变为（3）受理完时：
			if (state.equalsIgnoreCase("3")) {
				Map<String, String> touchuanJsonMap = new HashMap<String, String>();
				touchuanJsonMap.put("type", "2");// 1=通过透传，更改状态，2=通过透传，更新业务排队人数
				touchuanJsonMap.put("queueid", queue.getId());
				touchuanJsonMap.put("state", "3");// 状态值变为3
				touchuanJsonMap.put("window", employeeWindow);// 受理该业务的窗口号

				final String content2 = JsonUtil.map2json(touchuanJsonMap);
				// -开启推送线程
				new Thread(new Runnable() {
					public void run() {
						String appversion = user.getAppversion();
						String appType = appversion.substring(0, appversion.indexOf("_"));
						if (appType.equalsIgnoreCase("ios")) {
							try {
								PushToApple.pushToIos(deviceToken, "业务受理完毕，感谢您的信任。请对我们的服务进行评价。", content2);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						} else {
							try {
								PushToAndroid.pushToAndroid(clientid, "业务受理完毕，感谢您的信任。请对我们的服务进行评价。", content2);
							} catch (Exception e) {
								Trace.printStackTrace(e);
							}
						}
					}
				}).start();
				// 开启当前用户状态更改的透传线程
				new Thread(new Runnable() {
					public void run() {
						List<String> clientids = new ArrayList<String>();
						clientids.add(clientid);
						try {
							PushToAndroid.pushToAndroidList(content2, clientids);
						} catch (Exception e) {
							Trace.printStackTrace(e);
						}
					}
				}).start();
			}
			queue.setState(state);
			queue.setHandledby(currentEmployee);
			queue.setHandledon(System.currentTimeMillis());
			queue.setHandledwindow(employeeWindow);
			businessqueueService.modify(queue);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* *** 获取某用户业务队列列表 *** */
	@RequestMapping("app/queue/listByUser")
	public void listByUser(String userid, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("createdby", userid);
			// 查询我的【未取消和未评价】的队列
			List<BusinessQueue> items = this.businessqueueService.listByPerform(map);
			for (int i = 0; i < items.size(); i++) {
				Business business = businessService.findById(items.get(i).getBusinessid());
				// 查询我所取号的队列中【在排队】的人数
				Map<String, Object> businessMap = new HashMap<String, Object>();
				businessMap.put("businessid", items.get(i).getBusinessid());
				businessMap.put("createdon", items.get(i).getCreatedon());
				businessMap.put("state", "1");// 1=已取号
				business.setQueueCounter(businessqueueService.countFrontBy(businessMap));
				
				//1查询我所取号的业务所关联的评价指标
				//business.setIndexList(evaluationindexService.listAll());
				//2根据businessid查询业务所关联的评价指标ID列表
				Map<String, Object> indexMap=new HashMap<String, Object>();
				indexMap.put("businessid", items.get(i).getBusinessid());
				List<BusinessIndex> businessindexList=businessIndexService.listBy(indexMap);
				List<EvaluationIndex> evaluationindexList=new ArrayList<EvaluationIndex>();
				if(businessindexList!=null){
					for (int j = 0; j < businessindexList.size(); j++) {
						EvaluationIndex evaluationIndex=evaluationindexService.findById(businessindexList.get(j).getIndexid());
						evaluationindexList.add(evaluationIndex);
					}
				}
				business.setIndexList(evaluationindexList);
				 
				items.get(i).setBusiness(business);
			}
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
			AppIoUtil.closeSource(writer);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

	/* *** 获取某业务类型的所有队列列表 *** */
	@RequestMapping("app/queue/listByBusiness")
	public void listByBusiness(String businessid, HttpServletResponse response, HttpServletRequest request) {
		PrintWriter writer = AppIoUtil.getWriter(response);
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("businessid", businessid);
			map.put("state", "1");
			// 查询当前业务正【在排队】的列表
			List<BusinessQueue> items = this.businessqueueService.listBy(map);
			for (int i = 0; i < items.size(); i++) {
				Business business = businessService.findById(items.get(i).getBusinessid());
				// 查询当前业务正【在排队】的人数
				Map<String, String> businessMap = new HashMap<String, String>();
				businessMap.put("businessid", items.get(i).getBusinessid());
				businessMap.put("state", "1");// 1=已取号
				business.setQueueCounter(businessqueueService.countBy(businessMap));
				items.get(i).setBusiness(business);
			}
			String[] excludes = new String[] { "content", "orderby", "pid" };
			JsonConfig jsonConfig = AppUtils.filterField(excludes);
			String content = JsonUtil.list2json(items, jsonConfig);
			AppIoUtil.state(writer, Parameter.SUCCESS, Parameter.WIN, content);
			AppIoUtil.closeSource(writer);
		} catch (Exception e) {
			Trace.printStackTrace(e);
			AppIoUtil.state(writer, Parameter.DEFEAT, Parameter.ERROR);
		} finally {
			AppIoUtil.closeSource(writer);
		}
	}

}
