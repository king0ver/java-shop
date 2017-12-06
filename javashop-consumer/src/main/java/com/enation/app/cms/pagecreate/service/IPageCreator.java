package com.enation.app.cms.pagecreate.service;


/**
 * 
 * 静态页生成接口
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年8月29日 上午11:43:42
 */
public interface IPageCreator {
	/**
	 * 生成单个页面
	 * @param path	页面地址
	 * @param type	客户端类型
	 */
	public void createOne(String path,String type);
	/**
	 *  生成所有页面
	 */
	public void createAll();
	/**
	 *  生成商品页面
	 */
	public void createGoods();
	/**
	 *  生成首页
	 */
	public void createIndex();
	/**
	 * 生成帮助中心页面
	 */
	public void createHelp();
}
