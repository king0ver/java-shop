package com.enation.app.shop.aftersale.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 申请退货的商品
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月14日下午4:30:54
 */
@ApiModel(value="申请退货的商品", description = "申请退货的商品")
public class ApplyReturnGoods {

	@ApiModelProperty(value = "商品id" )
	private Integer goods_id;
	
	@ApiModelProperty(value = "产品id" )
	private Integer sku_id;
	
	@ApiModelProperty(value = "退货数量" )
	private Integer return_num;
	
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public Integer getSku_id() {
		return sku_id;
	}
	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}
	public Integer getReturn_num() {
		return return_num;
	}
	public void setReturn_num(Integer return_num) {
		this.return_num = return_num;
	}
	
	
	
}
