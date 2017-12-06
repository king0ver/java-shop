package com.enation.app.shop.waybill.model.waybilljson;
/**
 * 发送商品实体
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月12日 下午7:03:45
 */
public class Commodity {
	/** 商品名称 */
	private String GoodsName;
	/** 件数 */
	private Integer Goodsquantity; 
	/** 商品重量kg */
	private Double GoodsWeight; 
	/** 商品编码 */
	private String GoodsCode; 
	/** 商品描述 */
	private String GoodsDesc; 
	/** 商品体积m3 */
	private Double GoodsVol;  
	/** 商品价格 */
	private Double GoodsPrice; 
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	public Integer getGoodsquantity() {
		return Goodsquantity;
	}
	public void setGoodsquantity(Integer goodsquantity) {
		Goodsquantity = goodsquantity;
	}
	public Double getGoodsWeight() {
		return GoodsWeight;
	}
	public void setGoodsWeight(Double goodsWeight) {
		GoodsWeight = goodsWeight;
	}
	public String getGoodsCode() {
		return GoodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		GoodsCode = goodsCode;
	}
	public String getGoodsDesc() {
		return GoodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		GoodsDesc = goodsDesc;
	}
	public Double getGoodsVol() {
		return GoodsVol;
	}
	public void setGoodsVol(Double goodsVol) {
		GoodsVol = goodsVol;
	}
	public Double getGoodsPrice() {
		return GoodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		GoodsPrice = goodsPrice;
	}
	
	
	
	
}
