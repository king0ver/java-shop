package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 文本
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月11日 下午5:09:57
 */
@ApiModel
public class Text implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 298584744005441880L;
	
	@ApiModelProperty(value = "标题文字")
	private String title_text;
	
	@ApiModelProperty(value = "值")
	private String value;
	
	@ApiModelProperty(value = "类型")
	private String text_type;

	public String getTitle_text() {
		return title_text;
	}

	public void setTitle_text(String title_text) {
		this.title_text = title_text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText_type() {
		return text_type;
	}

	public void setText_type(String text_type) {
		this.text_type = text_type;
	}


}
