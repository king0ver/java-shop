package com.enation.app.cms.focuspic.model.po;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * cms首页焦点图实体
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月14日 上午11:09:32
 */
@ApiModel
public class CmsFocusPicture implements Serializable {

	private static final long serialVersionUID = 9004607441003876972L;
	/**首页焦点图id*/
	private Integer id; 
	// private String focus_content;//焦点图信息及操作;
	@NotNull(message = "图片地址不能为空")
	@ApiModelProperty(value = "焦点图图片地址url", example = "fs://asdfasdf")
	/**焦点图图片地址*/
	private String pic_url;
	@ApiModelProperty(value = "操作类型  ", example = "none")
	private String operation_type;
	@ApiModelProperty(value = "操作传入参数", example = "1")
	/**操作传入参数*/
	private String operation_param;
	/**操作url 传入参数,根据操作类型进行拼装生成operation_url*/
	private String operation_url;// 
	// @ApiModelProperty(value = "排序", example = "1")
	// private Integer sort;//排序
	/**客户端类型*/
	private String client_type;

	@PrimaryKeyField
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

	public String getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(String operation_type) {
		this.operation_type = operation_type;
	}

	public String getOperation_param() {
		return operation_param;
	}

	public void setOperation_param(String operation_param) {
		this.operation_param = operation_param;
	}

	public String getOperation_url() {
		return operation_url;
	}

	public void setOperation_url(String operation_url) {
		this.operation_url = operation_url;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

}
