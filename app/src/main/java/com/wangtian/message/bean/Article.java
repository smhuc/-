package com.wangtian.message.bean;

/**
 * 文章类
 * @author bingbing
 *
 */
public class Article implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String rowKey;   //唯一标识行键值    
	private String url;      //原始路径
	private String type;     //类型【新闻、论坛、博客、微博】
	private String title;    //标题
	private String author;   //作者
	private String origin;   //网站名 - 发布媒体
	private String publishDate;//发布时间,格式如：2015-05-10 13:10:30
	private int commentCount;//评论数量
	private int collectState;//0为未订阅  1为已订阅
	
	/**
	 * 内容,包含图片时，其中<img src将以/htmlimage开头
	 */
	private String content;
	
	/**
	 * 标签，值类型：json字符串，格式如下
	 * {label:[1,5,30],"语言":"中简","情感":"正面","境内外":"境外","级别":"高","概念":"主流媒体","方向":"001"}
	 * 其中label是订阅分类，值所属表【tab_user_hometag】的ID值
	 */
	private String tags;
	
	/**
	 * 评论数据，格式json字符串
	 */
	private String cmments;
	
	public int getCollectState() {
		return collectState;
	}
	public void setCollectState(int collectState) {
		this.collectState = collectState;
	}
	public String getRowKey() {
		return rowKey;
	}
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getCmments() {
		return cmments;
	}
	public void setCmments(String cmments) {
		this.cmments = cmments;
	}
}
