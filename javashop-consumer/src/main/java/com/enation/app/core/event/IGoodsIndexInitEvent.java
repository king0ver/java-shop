package com.enation.app.core.event;

/**
 * 商品索引生成事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:35:47
 */
public interface IGoodsIndexInitEvent {

	/**
	 * 创建商品索引
	 * @param str
	 */
	public void createGoodsIndex();
}
