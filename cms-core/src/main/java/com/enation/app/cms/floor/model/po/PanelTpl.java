package com.enation.app.cms.floor.model.po;

import javax.validation.constraints.NotNull;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 面板模板
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午9:17:35
 */
public class PanelTpl {
	@ApiModelProperty(hidden = true, value = "id")
	private Integer tpl_id;
	/**面板内容 */
	@ApiModelProperty("面板内容")
	@NotNull(message = "面板内容不能为空")
	private String tpl_content;
	/**面板名字 */
	@ApiModelProperty("面板名字")
	@NotNull(message = "面板名字不能为空")
	private String tpl_name;
	/**类型 main 主面板 normal 普通面板 */
	@ApiModelProperty("类型 main 主面板 normal 普通面板")
	@NotNull(message = "面板类型不能为空")
	private String tpl_type;
	/**客户端类型 */
	@ApiModelProperty("客户端类型")
	@NotNull(message = "客户端类型不能为空")
	private String client_type;

	@PrimaryKeyField
	public Integer getTpl_id() {
		return tpl_id;
	}

	public void setTpl_id(Integer tpl_id) {
		this.tpl_id = tpl_id;
	}

	public String getTpl_type() {
		return tpl_type;
	}

	public void setTpl_type(String tpl_type) {
		this.tpl_type = tpl_type;
	}

	public String getTpl_content() {
		return tpl_content;
	}

	public void setTpl_content(String tpl_content) {
		this.tpl_content = tpl_content;
	}

	public String getTpl_name() {
		return tpl_name;
	}

	public void setTpl_name(String tpl_name) {
		this.tpl_name = tpl_name;
	}

	public String getClient_type() {
		return client_type;
	}

	public void setClient_type(String client_type) {
		this.client_type = client_type;
	}

}
