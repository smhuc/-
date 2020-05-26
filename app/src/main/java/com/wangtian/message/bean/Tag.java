package com.wangtian.message.bean;

import java.io.Serializable;

public class Tag implements Serializable {

	private static final long serialVersionUID = 6960698301058965445L;

	private Integer id;//标签id
	private String name;//标签名
	private Integer groupid;//用户组id
	private long time;//用于缓存是保存时间
	
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	
	
	
	
}