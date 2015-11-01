package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class BusinessIndex implements Serializable{
	private String id;
	private String businessid;
	private String indexid;

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getBusinessid(){
	    return businessid;
	}
	public void setBusinessid(String businessid){
	    this.businessid = businessid;
	}
	public String getIndexid(){
	    return indexid;
	}
	public void setIndexid(String indexid){
	    this.indexid = indexid;
	}

}
