package com.enation.app.shop.goods.model.po;

import java.io.Serializable;
import java.util.List;

import com.enation.framework.database.PrimaryKeyField;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 规格实体
 * 
 * @author fk
 * @version v1.0 2017年3月22日 下午1:34:06
 */
@ApiModel
public class Specification implements Serializable {

	private static final long serialVersionUID = 8008287941472630697L;

	private List<SpecValue> valueList;
	/**
	 * 主键
	 */
	@ApiModelProperty(hidden = true)
	private Integer spec_id;
	/**
	 * 规格名称
	 */
	@ApiModelProperty(required = true, value = "规格名称")
	private String spec_name;
	/**
	 * 0文字 1图片
	 */
	@ApiModelProperty(required = true, value = "规格类型，0文字 1 图片")
	private Integer spec_type;
	/**
	 * 描述
	 */
	@ApiModelProperty(required = false, value = "规格备注")
	private String spec_memo;

	@ApiModelProperty(hidden = true, value = "是否已经被删除    ，0 删除   1  没有删除")
	private Integer disabled;
	@ApiModelProperty(value = "商家id")
	private Integer seller_id;

	@PrimaryKeyField
	public Integer getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(Integer specId) {
		spec_id = specId;
	}

	public String getSpec_name() {
		return spec_name;
	}

	public void setSpec_name(String specName) {
		spec_name = specName;
	}

	public Integer getSpec_type() {
		return spec_type;
	}

	public void setSpec_type(Integer specType) {
		spec_type = specType;
	}

	public String getSpec_memo() {
		return spec_memo;
	}

	public void setSpec_memo(String specMemo) {
		spec_memo = specMemo;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public List<SpecValue> getValueList() {
		return valueList;
	}

	public void setValueList(List<SpecValue> valueList) {
		this.valueList = valueList;
	}

	public Integer getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(Integer seller_id) {
		this.seller_id = seller_id;
	}

}
