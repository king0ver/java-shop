package com.enation.app.shop.trade.service;

import com.enation.app.shop.trade.support.CheckoutParam;


/**
 * 结算session操作接口
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月20日下午1:12:34
 */
public interface ICheckoutSession {

	
	/**
	 * 读取结算参数
	 * @param sessionid 
	 * @return
	 */
	public CheckoutParam read();


	/**
	 * 写入某项参数
	 * @param sessionid
	 * @param key
	 * @param value
	 */
	public void write(String key, Object value);

	

	/**
	 * 写入结算参数 
	 * @param sessionid
	 * @param param
	 */
	public void write(CheckoutParam param);

	
	
	
	/**
	 * session上锁
	 * @param sessionid
	 */
	public void lock();

	
	
	
	/**
	 * session解锁
	 * @param sessionid
	 */
	public void unlock();

	
	
	
	/**
	 * 检测session是否上锁了
	 * @param sessionid
	 * @return
	 */
	public boolean checkLock();
	
}