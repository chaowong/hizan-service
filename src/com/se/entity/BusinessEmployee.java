package com.se.entity;

import java.io.Serializable;
import java.math.BigDecimal;
@SuppressWarnings("serial")
public class BusinessEmployee implements Serializable{
	private String id;
	private String employeeid;
	private String window;
	private String businessid;
	private BigDecimal createdon;

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getEmployeeid(){
	    return employeeid;
	}
	public void setEmployeeid(String employeeid){
	    this.employeeid = employeeid;
	}
	public String getBusinessid(){
	    return businessid;
	}
	public void setBusinessid(String businessid){
	    this.businessid = businessid;
	}
	public BigDecimal getCreatedon(){
	    return createdon;
	}
	public void setCreatedon(BigDecimal createdon){
	    this.createdon = createdon;
	}
	public String getWindow() {
		return window;
	}
	public void setWindow(String window) {
		this.window = window;
	}

}
