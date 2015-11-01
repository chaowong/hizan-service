package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Advice implements Serializable{
	private String id;
	private String content;
	private String createdby;
	private long createdon;

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getContent(){
	    return content;
	}
	public void setContent(String content){
	    this.content = content;
	}
	public String getCreatedby(){
	    return createdby;
	}
	public void setCreatedby(String createdby){
	    this.createdby = createdby;
	}
	public long getCreatedon(){
	    return createdon;
	}
	public void setCreatedon(long createdon){
	    this.createdon = createdon;
	}

}
