package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Permission implements Serializable{
	private String id;
	private String name;
	private String roleid;
	private Role role;//一个权限对应一个角色

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
	public String getRoleid(){
	    return roleid;
	}
	public void setRoleid(String roleid){
	    this.roleid = roleid;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

}
