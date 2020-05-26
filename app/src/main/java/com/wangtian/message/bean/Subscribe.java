package com.wangtian.message.bean;

import java.io.Serializable;

public class Subscribe implements Serializable {

	private static final long serialVersionUID = 1L;


	private Integer id;  //主键编号
	
	private Integer groupId;  //用户组编号
	
	private String type;  //信息类型

	private String address;  //网站地址
	
	private String name;  //网站名称

	private String abroad;  //境内外
	
	private String orientation;  //方向
	
	private String region;  //地域
	
	private String concept;  //概念
	
	private String language;  //语种
	
	
	private String level;  //级别
	
	private Integer hometagId; //标签
	
    private Integer sub; //是否订阅 0-未订阅 1-已订阅

    public Integer getSub() {
		return sub;
	}

	public void setSub(Integer sub) {
		this.sub = sub;
	}
	

	public Integer getHometagId() {
		return hometagId;
	}

	public void setHometagId(Integer hometagId) {
		this.hometagId = hometagId;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbroad() {
		return abroad;
	}

	public void setAbroad(String abroad) {
		this.abroad = abroad;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	

	
}