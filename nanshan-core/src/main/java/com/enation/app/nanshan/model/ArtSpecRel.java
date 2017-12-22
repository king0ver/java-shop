package com.enation.app.nanshan.model;

import java.io.Serializable;

public class ArtSpecRel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int article_id;
	
	private int specval_id;

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public int getSpecval_id() {
		return specval_id;
	}

	public void setSpecval_id(int specval_id) {
		this.specval_id = specval_id;
	}
	
	
	

}
