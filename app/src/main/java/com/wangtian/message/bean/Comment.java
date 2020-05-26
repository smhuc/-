package com.wangtian.message.bean;
/**
 * 评论类
 * @author bingbing
 *
 */
public class Comment implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String rowkey;    //标识
	private String url;       //原始路径
	private String title;     //标题	
	private String publishDate; //发布时间,格式如：2015-05-10 13:10:30
	private String articleId; //所属文章	
	private String author;    //作者
	private int commentCount; //评论数量
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
	
	
	public String getRowkey() {
		return rowkey;
	}
	public void setRowkey(String rowkey) {
		this.rowkey = rowkey;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getArticleId() {
		return articleId;
	}
	public void setArticleId(String articleId) {
		this.articleId = articleId;
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
}
