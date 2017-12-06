package com.enation.app.cms.pagecreate.service;
/**
 * 
 * 静态页生成接口
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年9月1日 上午11:47:00
 */
public interface IPageCreateManager {
	/**
	 * 开始生成静态页
	 * @param choose_pages
	 * @return
	 */
	public boolean startCreate(String[] choose_pages) ;
}
