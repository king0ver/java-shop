package com.enation.app.shop.goods.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 缓存传递使用vo
 * @author fk
 * @version v1.0
 * 2017年6月9日 下午3:39:13
 */
public class GoodsCache implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7108562886992577724L;
	
	private int goodsid;
	
	private List<CacheSku> skuList;
	
	public int getGoodsid() {
		return goodsid;
	}
	
	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public List<CacheSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<CacheSku> skuList) {
		this.skuList = skuList;
	}
	
}
