package com.enation.app.cms.floor.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 单个图片对象
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:32:41
 */
public class SingleImage implements Serializable{

	private static final long serialVersionUID = -4306175051454597616L;

	/**图片url*/
	@ApiModelProperty(value = "图片url" )
	private String image_url;
	/**类型*/
	@ApiModelProperty(value = "type" )
	private String op_type;
	/**连接地址*/
	@ApiModelProperty(value = "连接地址" )
	private String op_value;

	public SingleImage() {
		
	}
	
	public SingleImage(String image_url, String op_type, String op_value) {
		super();
		this.image_url = image_url;
		this.op_type = op_type;
		this.op_value = op_value;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public String getOp_type() {
		return op_type;
	}

	public void setOp_type(String op_type) {
		this.op_type = op_type;
	}

	public String getOp_value() {
		return op_value;
	}

	public void setOp_value(String op_value) {
		this.op_value = op_value;
	}
	
}
