package com.enation.app.core.receiver;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IShopChangeEvent;


/**
 * 店铺变更消费者
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午5:17:25
 */
@Component
public class ShopChangeReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IShopChangeEvent> events;
	
	/**
	 * 店铺变更
	 * @param goodsChangeMsg
	 */
	public void shopChange(Integer shop_id){
		
		try{
			if(events!=null){
				for(IShopChangeEvent event : events){
					event.shopChange(shop_id);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理店铺变更消息出错",e);
			e.printStackTrace();
		}
	}
}
