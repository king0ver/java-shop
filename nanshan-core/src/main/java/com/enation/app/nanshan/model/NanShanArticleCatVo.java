package com.enation.app.nanshan.model;

import java.io.Serializable;
import java.util.List;


public class NanShanArticleCatVo extends  NanShanArticleCat implements Serializable {	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<NanShanArticleCatVo> notes;	
	
	private NanShanArticleCatVo parent;
	

	
	public NanShanArticleCatVo() {
		super();
	}
	
	public NanShanArticleCatVo(NanShanArticleCat nanShanArticleCat) {
		this.setCat_id(nanShanArticleCat.getCat_id());
		this.setCat_name(nanShanArticleCat.getCat_name());
		this.setParent_id(nanShanArticleCat.getParent_id());
		this.setIs_del(nanShanArticleCat.getIs_del());
		this.setPc_detail_template_uri(nanShanArticleCat.getPc_detail_template_uri());
		this.setPc_list_template_uri(nanShanArticleCat.getPc_list_template_uri());
		this.setWap_detail_template_uri(nanShanArticleCat.getWap_detail_template_uri());
		this.setWap_list_template_uri(nanShanArticleCat.getWap_list_template_uri());
		this.setPc_url(nanShanArticleCat.getPc_url());
		this.setWap_url(nanShanArticleCat.getWap_url());
	}
	public List<NanShanArticleCatVo> getNotes() {
		return notes;
	}
	public void setNotes(List<NanShanArticleCatVo> notes) {
		this.notes = notes;
	}
	public NanShanArticleCatVo getParent() {
		return parent;
	}
	public void setParent(NanShanArticleCatVo parent) {
		this.parent = parent;
	}
	
	
	
	
	
	
	


}
