package com.wangtian.message.bean;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 6960698301058965445L;

	private String id;//用户id
	private String name;//用户名
	private String password;//登陆密码
//	private Integer groupid;//用户组id
	private String isdel;//是否删除
	private String remark;//备注
	private String isAppManage;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getIsAppManage() {
		return isAppManage;
	}

	public void setIsAppManage(String isAppManage) {
		this.isAppManage = isAppManage;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
//	public Integer getGroupid() {
//		return groupid;
//	}
//	public void setGroupid(Integer groupid) {
//		this.groupid = groupid;
//	}
	public String getIsdel() {
		return isdel;
	}
	public void setIsdel(String isdel) {
		this.isdel = isdel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}