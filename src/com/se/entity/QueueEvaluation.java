package com.se.entity;

import java.io.Serializable;
@SuppressWarnings("serial")
public class QueueEvaluation implements Serializable{
	private String id;
	private String queueid;
	private String indexid;
	private Integer score;
	private String content;
	private String createdby;
	private long createdon;

	public String getId(){
	    return id;
	}
	public void setId(String id){
	    this.id = id;
	}
	public String getQueueid(){
	    return queueid;
	}
	public void setQueueid(String queueid){
	    this.queueid = queueid;
	}
	public String getIndexid(){
	    return indexid;
	}
	public void setIndexid(String indexid){
	    this.indexid = indexid;
	}
	public Integer getScore(){
	    return score;
	}
	public void setScore(Integer score){
	    this.score = score;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
