package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Category implements Serializable{
	private String id;
	private String title;
	private String description;
	private String parentid;

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getTitle(){
	    return title;
	}
	public void setTitle(String title){
	    this.title = title;
	}
	public String getDescription(){
	    return description;
	}
	public void setDescription(String description){
	    this.description = description;
	}
	public String getParentid(){
	    return parentid;
	}
	public void setParentid(String parentid){
	    this.parentid = parentid;
	}

}
