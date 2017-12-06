package com.enation.app.core.event;

import com.enation.app.shop.comments.model.vo.GoodsCommentMsg;

/**
 * 商品评论事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:45:08
 */
public interface IGoodsCommentEvent {

	/**
	 * 商品评论后执行
	 * @param goodsCommentMsg
	 */
	public void goodsComment(GoodsCommentMsg goodsCommentMsg);
}
