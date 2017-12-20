package com.enation.app.nanshan.model;

import java.io.Serializable;


/**  
*
* @Description:分类信息 
* @author luyanfen
* @date 2017年12月15日 下午4:00:46
*  
*/ 
public class NanShanArticleCat implements Serializable {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cat_id;
	private String cat_name;
	private String pc_url;
	private String wap_url;
	private String pc_list_template_uri;
	private String pc_detail_template_uri;
	private String wap_list_template_uri;
	private String wap_detail_template_uri;
	private int parent_id;
	private int is_del;
	
	

	public int getCat_id() {
		return cat_id;
	}
	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
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
	public String getPc_list_template_uri() {
		return pc_list_template_uri;
	}
	public void setPc_list_template_uri(String pc_list_template_uri) {
		this.pc_list_template_uri = pc_list_template_uri;
	}
	public String getPc_detail_template_uri() {
		return pc_detail_template_uri;
	}
	public void setPc_detail_template_uri(String pc_detail_template_uri) {
		this.pc_detail_template_uri = pc_detail_template_uri;
	}
	public String getWap_list_template_uri() {
		return wap_list_template_uri;
	}
	public void setWap_list_template_uri(String wap_list_template_uri) {
		this.wap_list_template_uri = wap_list_template_uri;
	}
	public String getWap_detail_template_uri() {
		return wap_detail_template_uri;
	}
	public void setWap_detail_template_uri(String wap_detail_template_uri) {
		this.wap_detail_template_uri = wap_detail_template_uri;
	}
	public int getParent_id() {
		return parent_id;
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
	
	
	
	


}
