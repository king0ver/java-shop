package com.enation.app.core.event;

import com.enation.framework.jms.support.goods.GoodsReasonMsg;

/**
 * 
 * 商品变化附带原因
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月20日 下午1:58:06
 */
public interface IGoodsReasonEvent {
	/**
	 * 商品附带原因的变化， 例：审核，管理员下架
	 * 
	 * @param goodsReasonMsg
	 */
	public void goodsReason(GoodsReasonMsg goodsReasonMsg);
}
