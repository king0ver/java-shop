package com.enation.app.shop.trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.service.ITradePriceManager;
import com.enation.app.shop.trade.support.OrderServiceConstant;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 订单价格管理的Redis实现
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年3月18日上午10:49:29
 */
@Service
public class RedisTradePriceManager implements ITradePriceManager {

	
	@Autowired
	private ICheckoutParamManager checkoutParamManager;
	
	@Autowired
	private ICache cache;
	
	
	public RedisTradePriceManager(){

	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ITradePriceManager#getTradePrice()
	 */
	@Override
	public PriceDetail getTradePrice(){
		
		String cacheKey =  this.getSessionKey();
		PriceDetail priceDetail =(PriceDetail) this.cache.get(cacheKey);
		if(priceDetail ==null){
			priceDetail = new PriceDetail();
		}
		return priceDetail;
	}


	@Override
	public void cleanPrice() {
		this.cache.remove(this.getSessionKey());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ITradePriceManager#pushPrice(com.enation.app.shop.trade.model.vo.PriceDetail)
	 */
	@Override
	public void pushPrice(PriceDetail price) {
		cache.put(this.getSessionKey(), price);
	}
	

	/**
	 * 根据会话id获取缓存的key<br>
	 * 如果会员没登陆使用会话id做为key<br>
	 * 如果会员登录了，用会员id做为key<br>
	 * @param sessionid 
	 * 		  会话id
	 * @return 缓存key
	 */
	private String getSessionKey( ){
		String cacheKey = OrderServiceConstant.PRICE_SESSION_ID_PREFIX+ThreadContextHolder.getSession().getId();
		
		//如果会员登陆了，则要以会员id为key
		Member member = UserConext.getCurrentMember();
		if (member != null) {
			cacheKey = OrderServiceConstant.PRICE_SESSION_ID_PREFIX+member.getMember_id();
		}
		
		return cacheKey;
	}
	
	
	private Member getMember(){
		return  UserConext.getCurrentMember();
	}

}