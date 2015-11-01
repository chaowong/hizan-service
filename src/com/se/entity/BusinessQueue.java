package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class BusinessQueue implements Serializable{
	private String id;
	private int queuenum;
	private String businessid;
	private Business business;
	private String state;
	private long createdon;
	private String createdby;
	private long handledon;
	private String handledby;
	private String handledwindow;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessid() {
		return businessid;
	}
	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public long getCreatedon() {
		return createdon;
	}
	public void setCreatedon(long createdon) {
		this.createdon = createdon;
	}
	public String getCreatedby() {
		return createdby;
	}
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}
	public long getHandledon() {
		return handledon;
	}
	public void setHandledon(long handledon) {
		this.handledon = handledon;
	}
	public String getHandledby() {
		return handledby;
	}
	public void setHandledby(String handledby) {
		this.handledby = handledby;
	}
	public Business getBusiness() {
		return business;
	}
	public void setBusiness(Business business) {
		this.business = business;
	}
	public int getQueuenum() {
		return queuenum;
	}
	public void setQueuenum(int queuenum) {
		this.queuenum = queuenum;
	}
	public String getHandledwindow() {
		return handledwindow;
	}
	public void setHandledwindow(String handledwindow) {
		this.handledwindow = handledwindow;
	}
}
