package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.ICategoryChangeEvent;
import com.enation.framework.jms.support.goods.CategoryChangeMsg;

/**
 * 分类 变更
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:47:22
 */
@Component
public class CategoryChangeReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<ICategoryChangeEvent> events;
	
	
	/**
	 * 商品分类变化
	 * @param goodsChangeMsg
	 */
	public void categoryChange(CategoryChangeMsg categoryChangeMsg){
		try {
			if(events!=null){
				for(ICategoryChangeEvent event : events){
					event.categoryChange(categoryChangeMsg);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理分类变化消息出错", e);
			e.printStackTrace();
		}
	}
}
