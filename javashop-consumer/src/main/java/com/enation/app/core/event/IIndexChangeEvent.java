package com.enation.app.core.event;

import com.enation.app.cms.floor.model.vo.CmsManageMsg;

/**
 * 商品分类变化事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:45:08
 */
public interface IIndexChangeEvent {

	/**
	 * 创建首页
	 */
	public void createIndexPage(CmsManageMsg operation);
}
