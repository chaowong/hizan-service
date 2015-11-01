package com.se.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@SuppressWarnings("serial")
public class User implements Serializable{
	private String id;
	private String password;
	private String mobile;
	private String birthday;
	private String constellation;
	private Integer sex;
	private String idcard;
	private String realname;
	private String nickname;
	private String avatar;
	private String slogan;
	private String email;
	private String qq;
	private String sinaweibo;
	private String wechat;
	private Integer level;
	private Integer vip;
	private String province;
	private String city;
	private String district;
	private String latitude;
	private String longitude;
	private Integer markcode;
	private String invitecode;
	private String clientid;
	private String devicetoken;
	private String appversion;
	private long registeredon;
	private long lastlogon;
	//private List<Role> roleList;//一个用户对应多个角色

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getPassword(){
	    return password;
	}
	public void setPassword(String password){
	    this.password = password;
	}
	public String getMobile(){
	    return mobile;
	}
	public void setMobile(String mobile){
	    this.mobile = mobile;
	}
	public String getBirthday(){
	    return birthday;
	}
	public void setBirthday(String birthday){
	    this.birthday = birthday;
	}
	public String getConstellation(){
	    return constellation;
	}
	public void setConstellation(String constellation){
	    this.constellation = constellation;
	}
	public Integer getSex(){
	    return sex;
	}
	public void setSex(Integer sex){
	    this.sex = sex;
	}
	public String getIdcard(){
	    return idcard;
	}
	public void setIdcard(String idcard){
	    this.idcard = idcard;
	}
	public String getRealname(){
	    return realname;
	}
	public void setRealname(String realname){
	    this.realname = realname;
	}
	public String getNickname(){
	    return nickname;
	}
	public void setNickname(String nickname){
	    this.nickname = nickname;
	}
	public String getAvatar(){
	    return avatar;
	}
	public void setAvatar(String avatar){
	    this.avatar = avatar;
	}
	public String getSlogan(){
	    return slogan;
	}
	public void setSlogan(String slogan){
	    this.slogan = slogan;
	}
	public String getEmail(){
	    return email;
	}
	public void setEmail(String email){
	    this.email = email;
	}
	public String getQq(){
	    return qq;
	}
	public void setQq(String qq){
	    this.qq = qq;
	}
	public String getSinaweibo(){
	    return sinaweibo;
	}
	public void setSinaweibo(String sinaweibo){
	    this.sinaweibo = sinaweibo;
	}
	public String getWechat(){
	    return wechat;
	}
	public void setWechat(String wechat){
	    this.wechat = wechat;
	}
	public Integer getLevel(){
	    return level;
	}
	public void setLevel(Integer level){
	    this.level = level;
	}
	public Integer getVip(){
	    return vip;
	}
	public void setVip(Integer vip){
	    this.vip = vip;
	}
	public String getProvince(){
	    return province;
	}
	public void setProvince(String province){
	    this.province = province;
	}
	public String getCity(){
	    return city;
	}
	public void setCity(String city){
	    this.city = city;
	}
	public String getDistrict(){
	    return district;
	}
	public void setDistrict(String district){
	    this.district = district;
	}
	public String getLatitude(){
	    return latitude;
	}
	public void setLatitude(String latitude){
	    this.latitude = latitude;
	}
	public String getLongitude(){
	    return longitude;
	}
	public void setLongitude(String longitude){
	    this.longitude = longitude;
	}
	public Integer getMarkcode(){
	    return markcode;
	}
	public void setMarkcode(Integer markcode){
	    this.markcode = markcode;
	}
	public String getInvitecode(){
	    return invitecode;
	}
	public void setInvitecode(String invitecode){
	    this.invitecode = invitecode;
	}
	public String getDevicetoken(){
	    return devicetoken;
	}
	public void setDevicetoken(String devicetoken){
	    this.devicetoken = devicetoken;
	}
	public String getAppversion(){
	    return appversion;
	}
	public void setAppversion(String appversion){
	    this.appversion = appversion;
	}
	public long getRegisteredon(){
	    return registeredon;
	}
	public void setRegisteredon(long registeredon){
	    this.registeredon = registeredon;
	}
	public long getLastlogon(){
	    return lastlogon;
	}
	public void setLastlogon(long lastlogon){
	    this.lastlogon = lastlogon;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	/*public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public Set<String> getRolesName(){
		List<Role> roles=getRoleList();
		Set<String> set=new HashSet<String>();
		for (Role role:roles) {
			set.add(role.getName());
		}
		return set;
	}*/

}
