package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class EvaluationIndex implements Serializable{
	private String id;
	private String title;
	private String description;
	private Integer value;
	private String orderby;
	private long createdon;
	private String createdby;

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
	public Integer getValue(){
	    return value;
	}
	public void setValue(Integer value){
	    this.value = value;
	}
	public String getOrderby(){
	    return orderby;
	}
	public void setOrderby(String orderby){
	    this.orderby = orderby;
	}
	public long getCreatedon(){
	    return createdon;
	}
	public void setCreatedon(long createdon){
	    this.createdon = createdon;
	}
	public String getCreatedby(){
	    return createdby;
	}
	public void setCreatedby(String createdby){
	    this.createdby = createdby;
	}

}
