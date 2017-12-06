package com.enation.app.shop.aftersale.model.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 入库
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年5月22日下午11:58:36
 */
@ApiModel( description = "入库单")
public class StockIn {
	
	
	@ApiModelProperty(value = "退款单号" )
	private String sn ;
	
	@ApiModelProperty(value = "退货商品列表" )
	private List<ApplyReturnGoods> goodsList;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public List<ApplyReturnGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<ApplyReturnGoods> goodsList) {
		this.goodsList = goodsList;
	}
	
	
	
	
}
