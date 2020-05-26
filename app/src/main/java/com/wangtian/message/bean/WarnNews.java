package com.wangtian.message.bean;

public class WarnNews {

	private Integer id;				// 唯一标识行键值
	
	private Integer topicid; 	//所属专题

	private String articleid; 		//文章行键值
	
	private String article_title; 		//文章标题	
	
	private String article_publish_date;		//文章发布时间
	
	private String article_origin;   //网站名

	private Integer gourp_articleid;   //网站编号
	
	private Integer type; 		//网站类型
	
	private String warnid; 		//告警规则，多个使用逗号分隔
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTopicid() {
		return topicid;
	}
	public void setTopicid(Integer topicid) {
		this.topicid = topicid;
	}
	public String getArticleid() {
		return articleid;
	}
	public void setArticleid(String articleid) {
		this.articleid = articleid;
	}
	public String getArticle_title() {
		return article_title;
	}
	public void setArticle_title(String article_title) {
		this.article_title = article_title;
	}
	public String getArticle_publish_date() {
		return article_publish_date;
	}
	public void setArticle_publish_date(String article_publish_date) {
		this.article_publish_date = article_publish_date;
	}
	public String getArticle_origin() {
		return article_origin;
	}
	public void setArticle_origin(String article_origin) {
		this.article_origin = article_origin;
	}
	public Integer getGourp_articleid() {
		return gourp_articleid;
	}
	public void setGourp_articleid(Integer gourp_articleid) {
		this.gourp_articleid = gourp_articleid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getWarnid() {
		return warnid;
	}
	public void setWarnid(String warnid) {
		this.warnid = warnid;
	}

}
