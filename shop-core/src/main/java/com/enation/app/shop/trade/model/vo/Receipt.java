package com.enation.app.shop.trade.model.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 发票
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月24日下午9:13:53
 */
@ApiModel( description = "发票")
public class Receipt  implements Serializable{
	
	private static final long serialVersionUID = -6389742728556211209L;
	
	@ApiModelProperty(value = "是否需要发票" )
	private String need_receipt;
	
	@NotNull(message="抬头不能为空")
	@ApiModelProperty(value = "抬头" )
	private String title;
	
	@NotNull(message="内容不能为空")
	@ApiModelProperty(value = "内容" )
	private String content;
	
	@NotNull(message="税号不能为空")
	@ApiModelProperty(value = "税号" )
	private String duty_invoice;
	
	@ApiModelProperty(value = "类型" )
	private String type;
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getNeed_receipt() {
		return need_receipt;
	}
	public void setNeed_receipt(String need_receipt) {
		this.need_receipt = need_receipt;
	}
	public String getDuty_invoice() {
		return duty_invoice;
	}
	public void setDuty_invoice(String duty_invoice) {
		this.duty_invoice = duty_invoice;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
