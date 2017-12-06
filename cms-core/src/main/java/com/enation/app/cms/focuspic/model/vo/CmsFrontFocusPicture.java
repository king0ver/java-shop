package com.enation.app.cms.focuspic.model.vo;

import java.io.Serializable;
/**
 * 
 * 焦点图实体 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 下午1:20:44
 */
public class CmsFrontFocusPicture implements Serializable{

	private static final long serialVersionUID = 2275630447958003052L;
	/**焦点图id*/
	private Integer id;
	/**图片url*/
	private String pic_url;
	/**跳转url*/
	private String operation_url;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	
	public String getOperation_url() {
		return operation_url;
	}
	public void setOperation_url(String operation_url) {
		this.operation_url = operation_url;
	}

}
