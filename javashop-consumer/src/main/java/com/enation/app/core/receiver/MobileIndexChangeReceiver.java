package com.enation.app.core.receiver;


import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.cms.floor.model.vo.CmsManageMsg;
import com.enation.app.core.event.IMobileIndexChangeEvent;
/**
 * 
 * 移动端首页生成
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 下午3:41:18
 */
@Component
public class MobileIndexChangeReceiver {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired(required=false)
	private List<IMobileIndexChangeEvent> events;
	
	/**
	 * 生成首页
	 */
	public void mobileIndexChange(CmsManageMsg operation) {
		
		try{
			if(events!=null){
				for(IMobileIndexChangeEvent event : events){
					event.mobileIndexChange(operation);
				}
			}
		} catch (Exception e) {
			this.logger.error("处理移动端首页生成出错",e);
			e.printStackTrace();
		}
	}

}
