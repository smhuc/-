package com.wangtian.message.bean;


public class Collect {

	private Integer id;				// 唯一标识行键值
	
	private Integer userid; 	//用户

	private Integer source; 		//文章来源：0-新闻热点 1-预警	
	
	private String url; 		//url	
	
	private String articleid;		//文章行键值

	private String type;   //网站类型

	private Integer isdel;   //删除状态 0-未删除 1-删除

	private String name; 		//标题名称	

	private String article_publish_date; 		//文章发布时间
	
	
	private String origin;   //网站名 - 发布媒体
	private String publishDate;//发布时间,格式如：2015-05-10 13:10:30
	private int commentCount;//评论数量
	/**
	 * 标签，值类型：json字符串，格式如下
	 * {label:[1,5,30],"语言":"中简","情感":"正面","境内外":"境外","级别":"高","概念":"主流媒体","方向":"001"}
	 * 其中label是订阅分类，值所属表【tab_user_hometag】的ID值
	 */
	private String tags;
	

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getIsdel() {
		return isdel;
	}
	public void setIsdel(Integer isdel) {
		this.isdel = isdel;
	}


	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String getPublishDate() {
		return publishDate;
	}


	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}


	public int getCommentCount() {
		return commentCount;
	}


	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArticle_publish_date() {
		return article_publish_date;
	}
	public void setArticle_publish_date(String article_publish_date) {
		this.article_publish_date = article_publish_date;
	}
}
