package com.enation.app.shop.goodssearch.service;
/**
 * 
 * 商品索引发消息接口
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月22日 上午9:59:41
 */
public interface IGoodsIndexSendManager {
	/**
	 * 下达生成索引任务
	 * @return 任务是否下达成功
	 */
	public boolean startCreate() ;
}
