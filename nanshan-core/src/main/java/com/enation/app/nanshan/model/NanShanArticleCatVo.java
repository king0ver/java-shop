package com.enation.app.nanshan.model;

import java.io.Serializable;
import java.util.List;


public class NanShanArticleCatVo extends  ArticleCat implements Serializable {	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<NanShanArticleCatVo> notes;	
	
	private NanShanArticleCatVo parent;
	

	
	public NanShanArticleCatVo() {
		super();
	}
	
	public NanShanArticleCatVo(ArticleCat nanShanArticleCat) {
		this.setCat_id(nanShanArticleCat.getCat_id());
		this.setCat_name(nanShanArticleCat.getCat_name());
		this.setParent_id(nanShanArticleCat.getParent_id());
		this.setIs_del(nanShanArticleCat.getIs_del());
	
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
