package com.se.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;
@SuppressWarnings("serial")
public class Role implements Serializable{
	private String id;
	private String name;
	private List<User> userList;//一个角色对应多个用户
	private List<Permission> permissionList;//一个角色对应多个权限

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getName(){
	    return name;
	}
	public void setName(String name){
	    this.name = name;
	}
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}
	public List<String> getPermissionsName(){
		List<String> list= new ArrayList<String>();
		List<Permission> permissionList=getPermissionList();
		for (Permission permission : permissionList) {
			list.add(permission.getName());
		}
		return list;
	}

}
