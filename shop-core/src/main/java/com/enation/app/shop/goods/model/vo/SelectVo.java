package com.enation.app.shop.goods.model.vo;

/**
 * 用于select标签使用
 * @author fk
 * @version v1.0
 * 2017年5月10日 下午7:37:32
 */
public class SelectVo {
	
	private Integer id;
	private String text;
	private boolean selected;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
