package com.enation.app.nanshan.model;

import java.io.Serializable;



/**  
* @Title: NanShanArticleVo.java
* @Package com.enation.app.nanshan.model
* @Description: 文章
* @author luyanfen
* @date 2017年12月14日 下午3:28:07
*  
*/ 
public class NanShanArticleVo implements Serializable  { 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id; 
	private String title;  //标题
	private int cat_id;    //分类id
	private String url;    //链接
	private long create_time; //时间
	private String summary;  // 摘要
	private String pic_url; //图片链接
	private int is_del; //是否删除
	private String content; //文章内容，json格式
	private int content_id;
	private String cat_name;
	
	
	public int getContent_id() {
		return content_id;
	}
	public void setContent_id(int content_id) {
		this.content_id = content_id;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public int getIs_del() {
		return is_del;
	}
	public void setIs_del(int is_del) {
		this.is_del = is_del;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	



}
