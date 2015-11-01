package com.se.entity;

import java.io.Serializable;
import java.util.List;
@SuppressWarnings("serial")
public class Business implements Serializable{
	private String id;
	private String title;
	private String description;
	private String windows;
	private String state;
	private long queueCounter;
	private List<EvaluationIndex> indexList;
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
	public long getQueueCounter() {
		return queueCounter;
	}
	public void setQueueCounter(long queueCounter) {
		this.queueCounter = queueCounter;
	}
	public List<EvaluationIndex> getIndexList() {
		return indexList;
	}
	public void setIndexList(List<EvaluationIndex> indexList) {
		this.indexList = indexList;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getWindows() {
		return windows;
	}
	public void setWindows(String windows) {
		this.windows = windows;
	}

}
