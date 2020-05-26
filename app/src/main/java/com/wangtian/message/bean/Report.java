package com.wangtian.message.bean;
/**
 * 信息报告
 * @author chen
 *
 */
public class Report {
	private String createTime; 	//创建时间
	private int groupid;		//用户组id
	private int id;				//标签
	private String name;		//名字
	private String path;		//路径
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public int getGroupid() {
		return groupid;
	}
	public void setGroupid(int groupid) {
		this.groupid = groupid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
