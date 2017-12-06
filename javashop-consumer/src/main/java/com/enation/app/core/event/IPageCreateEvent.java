package com.enation.app.core.event;

/**
 * 页面生成事件
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月29日 下午4:59:53
 */
public interface IPageCreateEvent {

	/**
	 * 生成
	 * @param choose_pages
	 */
	public void createPage(String[] choose_pages);
}
