package com.enation.app.nanshan.model;

import java.io.Serializable;

public class ArticleExt implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int article_id;
	int reserve_num;
	int reserved_num;
	long expiry_date;
	String act_name;
	String act_cost;
	String act_address;
	
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
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
	public long getExpiry_date() {
		return expiry_date;
	}
	public void setExpiry_date(long expiry_date) {
		this.expiry_date = expiry_date;
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
	
	

}
