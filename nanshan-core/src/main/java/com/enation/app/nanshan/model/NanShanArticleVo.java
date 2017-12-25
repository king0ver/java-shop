package com.enation.app.nanshan.model;

import java.io.Serializable;
import java.util.List;



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
	private String specValIds;
	
	
	private int reserve_num;
	private int reserved_num;
	private String  expiryDate;
	private String act_name;
	private String act_cost;
	private String act_address;
	
	private String catIds;
	

	public String getCatIds() {
		return catIds;
	}
	public void setCatIds(String catIds) {
		this.catIds = catIds;
	}
	public int getReserve_num() {
		return reserve_num;
	}
	public void setReserve_num(int reserve_num) {
		this.reserve_num = reserve_num;
	}
	public int getReserved_num() {
		return reserved_num;
	}
	public void setReserved_num(int reserved_num) {
		this.reserved_num = reserved_num;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public String getAct_cost() {
		return act_cost;
	}
	public void setAct_cost(String act_cost) {
		this.act_cost = act_cost;
	}
	public String getAct_address() {
		return act_address;
	}
	public void setAct_address(String act_address) {
		this.act_address = act_address;
	}
	public String getSpecValIds() {
		return specValIds;
	}
	public void setSpecValIds(String specValIds) {
		this.specValIds = specValIds;
	}
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
