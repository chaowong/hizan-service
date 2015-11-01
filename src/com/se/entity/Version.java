package com.se.entity;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class Version implements Serializable{
	private String id;
	private String content;
	private String version;
	private String apptype;
	private String isforced;
	private String download;
	private Date createdon;

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
	public String getVersion(){
	    return version;
	}
	public void setVersion(String version){
	    this.version = version;
	}
	public String getApptype(){
	    return apptype;
	}
	public void setApptype(String apptype){
	    this.apptype = apptype;
	}
	public String getIsforced(){
	    return isforced;
	}
	public void setIsforced(String isforced){
	    this.isforced = isforced;
	}
	public String getDownload(){
	    return download;
	}
	public void setDownload(String download){
	    this.download = download;
	}
	public Date getCreatedon(){
	    return createdon;
	}
	public void setCreatedon(Date createdon){
	    this.createdon = createdon;
	}

}
