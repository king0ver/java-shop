package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IGoodsCommentEvent;
import com.enation.app.shop.comments.model.vo.GoodsCommentMsg;

/**
 * 商品评论
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:47:22
 */
@Component
public class GoodsCommentReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IGoodsCommentEvent> events;
	
	/**
	 * 商品评论
	 * @param goodsCommentMsg
	 */
	public void commentComplete(GoodsCommentMsg goodsCommentMsg){
		try {
			if(events!=null){
				for(IGoodsCommentEvent event : events){
					event.goodsComment(goodsCommentMsg);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理商品评论消息出错", e);
			e.printStackTrace();
		}
	}
}
