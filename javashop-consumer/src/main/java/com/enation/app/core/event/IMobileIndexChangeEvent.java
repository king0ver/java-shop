package com.enation.app.core.event;

import com.enation.app.cms.floor.model.vo.CmsManageMsg;

/**
 * 移动端首页变化
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:45:08
 */
public interface IMobileIndexChangeEvent {

	/**
	 * 创建首页
	 */
	public void mobileIndexChange(CmsManageMsg operation);
}
