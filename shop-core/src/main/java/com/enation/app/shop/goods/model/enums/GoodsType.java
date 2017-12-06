package com.enation.app.shop.goods.model.enums;

/**
 * 
 * 商品类型
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月31日 下午2:52:37
 */
public enum GoodsType {
	normal("正常商品"), 
	point("积分商品");

	private String description;

	GoodsType(String _description) {
		this.description = _description;

	}
}
