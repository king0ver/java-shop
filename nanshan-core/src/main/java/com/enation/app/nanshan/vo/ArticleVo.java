package com.enation.app.nanshan.vo;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**
 * 文章信息
 * @author jianjianming
 * @version $Id: ArticleVo.java,v 0.1 2017年12月21日 下午9:44:25$
 */
public class ArticleVo implements Serializable {

	private static final long serialVersionUID = 3854836881136684562L;
	
	private Integer id;
	/**
	 * 标题
	 */
	private String title;  
	/**
	 * 分类id
	 */
	private int catId;
	/**
	 * 链接
	 */
	private String url;
	/**
	 * 图片地址
	 */
	private String imgUrl;
	/**
	 * 摘要
	 */
	private String summary;

	/**
	 * 文件内容
	 */
	private JSONObject content;


	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}
}
