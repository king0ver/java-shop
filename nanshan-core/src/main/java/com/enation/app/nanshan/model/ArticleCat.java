package com.enation.app.nanshan.model;

import java.io.Serializable;


/**  
*
* @Description:分类信息 
* @author luyanfen
* @date 2017年12月15日 下午4:00:46
*  
*/ 
public class ArticleCat implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long cat_id;
	private String cat_name;
	private String pc_url;
	private String wap_url;
	private long parent_id;
	private int is_del;
	private String spec_id;
	private String wap_cat_name;
	private String pc_icon;
	private String wap_icon;
	
	
	

	
	public long getCat_id() {
		return cat_id;
	}
	public void setCat_id(long cat_id) {
		this.cat_id = cat_id;
	}
	public long getParent_id() {
		return parent_id;
	}
	public void setParent_id(long parent_id) {
		this.parent_id = parent_id;
	}
	public String getCat_name() {
		return cat_name;
	}
	public void setCat_name(String cat_name) {
		this.cat_name = cat_name;
	}
	public String getPc_url() {
		return pc_url;
	}
	public void setPc_url(String pc_url) {
		this.pc_url = pc_url;
	}
	public String getWap_url() {
		return wap_url;
	}
	public void setWap_url(String wap_url) {
		this.wap_url = wap_url;
	}

	
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public int getIs_del() {
		return is_del;
	}
	public void setIs_del(int is_del) {
		this.is_del = is_del;
	}
	public String getSpec_id() {
		return spec_id;
	}
	public void setSpec_id(String spec_id) {
		this.spec_id = spec_id;
	}
	public String getWap_cat_name() {
		return wap_cat_name;
	}
	public void setWap_cat_name(String wap_cat_name) {
		this.wap_cat_name = wap_cat_name;
	}
	public String getPc_icon() {
		return pc_icon;
	}
	public void setPc_icon(String pc_icon) {
		this.pc_icon = pc_icon;
	}
	public String getWap_icon() {
		return wap_icon;
	}
	public void setWap_icon(String wap_icon) {
		this.wap_icon = wap_icon;
	}
	
	
	
	
	
	


}
