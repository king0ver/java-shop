package com.enation.app.shop.promotion.tool.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.bonus.model.StoreBonusType;
import com.enation.app.shop.promotion.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.promotion.exchange.model.po.Exchange;
import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuy;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager;
import com.enation.app.shop.promotion.minus.model.vo.MinusVo;
import com.enation.app.shop.promotion.minus.service.IMinusManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.po.PromotionGoods;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

/**
 * 活动商品对照表实现类
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年08月25日17:05:12
 */
@Service
public class PromotionGoodsManager implements IPromotionGoodsManager {

	@Autowired
	private IDaoSupport daoSupport;	
	
	@Autowired
	private ICache cache;
	
	@Autowired
	private IExchangeManager exchangeManager;
	
	@Autowired
	private IGroupBuyManager groupBuyManager;
	
	@Autowired
	private  IFullDiscountManager fullDiscountManager;
	
	@Autowired
	private IMinusManager minusManager;
	
	@Autowired
	private IHalfPriceManager halfPriceManager;
	
	@Autowired
	private IB2b2cBonusManager bonusManager;
	
	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager;
	
	private final String PROMOTION_KEY = "promotion";
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.promotion.service.IPromotionGoodsManager#add(java.util.List)
	 */
	@Override
	public void add(List<PromotionGoodsVo> list) {
		if(list.isEmpty()){
			return;
		}
		/**
		 * 因为 Spring jdbctemplate 没有提供批量插入的方法,
		 * 使用 insert into table_name (xxx1,xxx2) VALUES (1,2),(3,3) 拼接参数，会有sql注入的问题。
		 * 所以经过和架构师的沟通，此操作不是高频操作，可以一个一个插入数据库。
		 */
		for (PromotionGoodsVo promotionGoodsVo : list) {
			PromotionGoods promotionGoods = new PromotionGoods();
			BeanUtils.copyProperties(promotionGoodsVo, promotionGoods);
			this.daoSupport.insert("es_promotion_goods", promotionGoods);
			//将商品活动的缓存信息删除
			cache.remove(PROMOTION_KEY+"_"+promotionGoodsVo.getGoods_id());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.promotion.service.IPromotionGoodsManager#delete(java.lang.Integer, java.lang.String)
	 */
	@Override
	public void delete(Integer activity_id, String promotion_type) {
		String sql = "select * from es_promotion_goods WHERE activity_id= ? and promotion_type= ? ";
		List<PromotionGoodsVo> queryForList = this.daoSupport.queryForList(sql, PromotionGoodsVo.class, activity_id,promotion_type);
		sql = "DELETE FROM es_promotion_goods WHERE activity_id=? and promotion_type= ? ";
		this.daoSupport.execute(sql, activity_id,promotion_type);
		//将商品活动的缓存信息删除
		for (PromotionGoodsVo promotionGoodsVo : queryForList) {
			cache.remove(PROMOTION_KEY+"_"+promotionGoodsVo.getGoods_id());
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.promotion.service.IPromotionGoodsManager#edit(java.util.List, java.lang.Integer, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(List<PromotionGoodsVo> list, Integer activity_id, String promotion_type) {
		this.delete(activity_id, promotion_type);
		this.add(list);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.service.IPromotionGoodsManager#getPromotion(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<PromotionGoodsVo> getPromotion(Integer goods_id) {
		
		long currTime = DateUtil.getDateline();
		//从缓存中读取商品活动信息
		List<PromotionGoodsVo> list = (List<PromotionGoodsVo>) cache.get(PROMOTION_KEY+"_"+goods_id);
		List<PromotionGoodsVo> resultList = null;
		//如果为空则从数据库中查询，存入缓存
		if(list==null||list.size()==0){
			String sql = "select distinct goods_id , start_time , end_time , activity_id , promotion_type , title  from es_promotion_goods where goods_id=? and start_time<=? and end_time>=?";
			resultList =  this.daoSupport.queryForList(sql, PromotionGoodsVo.class, goods_id,currTime,currTime);
			for (PromotionGoodsVo promotionGoodsVo : resultList) {
				if(promotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.EXCHANGE.getType())) {//积分换购
					Exchange exchange = exchangeManager.getSettingToShow(goods_id,promotionGoodsVo.getActivity_id());
					promotionGoodsVo.setExchange(exchange);
					continue;
				}
				if(promotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.GROUPBUY.getType())) {//团购
					GroupBuy groupBuy = groupBuyManager.getBuyGoodsId(goods_id,promotionGoodsVo.getActivity_id());
					promotionGoodsVo.setGroupBuy(groupBuy);
					continue;
				}
				if(promotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.FULLDISCOUNT.getType())) {//满优惠活动
					
					FullDiscountVo fullDiscountVo = this.fullDiscountManager.get(promotionGoodsVo.getActivity_id());
					promotionGoodsVo.setFullDiscountVo(fullDiscountVo);
					
					if(fullDiscountVo.getIs_send_bonus() == 1) {//有优惠券
						StoreBonusType storeBonusType = bonusManager.get(fullDiscountVo.getBonus_id());
						promotionGoodsVo.setStoreBonusType(storeBonusType);
					}
					if(fullDiscountVo.getIs_send_gift() == 1) {
						FullDiscountGift fullDiscountGift = fullDiscountGiftManager.get(fullDiscountVo.getGift_id());
						promotionGoodsVo.setFullDiscountGift(fullDiscountGift);
					}
					continue;
				}
				if(promotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.MINUS.getType())) {//单品立减活动
					MinusVo minusVo = this.minusManager.get(promotionGoodsVo.getActivity_id());
					promotionGoodsVo.setMinusVo(minusVo);
					continue;
				}
				if(promotionGoodsVo.getPromotion_type().equals(PromotionTypeEnum.HALFPRICE.getType())) {//第二件半价活动
					HalfPriceVo halfPriceVo = halfPriceManager.get(promotionGoodsVo.getActivity_id());
					promotionGoodsVo.setHalfPriceVo(halfPriceVo);
					continue;
				}
			}
			cache.put(PROMOTION_KEY+"_"+goods_id, resultList);
		}else{
			//如果不为空在判断活动是否在进行
			resultList = new ArrayList<>();
			for(PromotionGoodsVo promotion:list){
				if(currTime>promotion.getStart_time() && currTime<promotion.getEnd_time()){
					resultList.add(promotion);
				}
			}
		}
		return resultList;
	}

}
