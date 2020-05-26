package com.wangtian.message.bean;

import java.io.Serializable;

public class Warn implements Serializable {

	private static final long serialVersionUID = 6960698301058965445L;

	private Integer id;				// 唯一标识行键值
	private Integer groupid; 	//所属用户组	
	private String name; 		//名称	
	private String desc; 		//描述	
	private Integer enable;		//是否启用	
	private Integer remove;   //是否删除
	private String firstStartTime;   //第一次启动时间戳
	private String startDate; 		//匹配起始时间点
	private String rule; 		//预计规则
	private String keyword; 		//关键词
	private String articlecount; 		//今日发布消息总量
	private String warnid; 		//规则id
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getGroupid() {
		return groupid;
	}
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getEnable() {
		return enable;
	}
	public void setEnable(Integer enable) {
		this.enable = enable;
	}
	public Integer getRemove() {
		return remove;
	}
	public void setRemove(Integer remove) {
		this.remove = remove;
	}
	public 	String getFirstStartTime() {
		return firstStartTime;
	}
	public void setFirstStartTime(String firstStartTime) {
		this.firstStartTime = firstStartTime;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getArticlecount() {
		return articlecount;
	}
	public void setArticlecount(String articlecount) {
		this.articlecount = articlecount;
	}
	public String getWarnid() {
		return warnid;
	}
	public void setWarnid(String warnid) {
		this.warnid = warnid;
	}
	
	
	
	
}