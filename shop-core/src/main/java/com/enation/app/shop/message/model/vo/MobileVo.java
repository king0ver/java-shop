package com.enation.app.shop.message.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 发送短信所需参数
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月25日 下午7:17:14
 */
@ApiModel(value="MobileVo", description = "发送短信所需参数")
public class MobileVo {

	@ApiModelProperty(value = "发送手机" )
	private String mobile;
	@ApiModelProperty(value = "发送内容" )
	private String content;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
