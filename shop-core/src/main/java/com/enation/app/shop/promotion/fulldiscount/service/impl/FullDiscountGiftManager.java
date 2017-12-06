package com.enation.app.shop.promotion.fulldiscount.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
/**
 * 
 * 满优惠促销赠品管理类
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午5:52:27
 */
@Service
public class FullDiscountGiftManager implements IFullDiscountGiftManager {
	@Autowired
	private IDaoSupport  daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager#list(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page list(String keyword, Integer shop_id, Integer pageNo, Integer pageSize) {
		String sql = "select * from es_full_discount_gift where shop_id = ? and disabled = 0";
		
		//如果关键字不为空
		if(keyword != null && !StringUtil.isEmpty(keyword)){
			sql += " and gift_name like '%" + keyword + "%'";
		}
		
		sql += " order by create_time desc";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, shop_id);
		return page;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager#add(com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift)
	 */
	@Override
	public void add(FullDiscountGift fullDiscountGift ) {
		this.daoSupport.insert("es_full_discount_gift", fullDiscountGift);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager#edit(com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift)
	 */
	@Override
	public void edit(FullDiscountGift fullDiscountGift ) {
		this.daoSupport.update("es_full_discount_gift", fullDiscountGift, "gift_id="+fullDiscountGift.getGift_id());
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager#get(java.lang.Integer)
	 */
	@Override
	public FullDiscountGift get(Integer gift_id) {
		String sql = "select * from es_full_discount_gift where gift_id = ? and disabled = 0";
		return this.daoSupport.queryForObject(sql, FullDiscountGift.class, gift_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager#listAll(java.lang.Integer)
	 */
	@Override
	public List<FullDiscountGift> listAll(Integer shop_id) {
		String sql = "select * from es_full_discount_gift where shop_id = ? and disabled = 0";
		List<FullDiscountGift> list = this.daoSupport.queryForList(sql, FullDiscountGift.class, shop_id);
		return list;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager#checkGiftInAct(java.lang.Integer)
	 */
	@Override
	public int checkGiftInAct(Integer gift_id) {
		String sql = "select count(*) from es_full_discount  where end_time > ? and ad.gift_id = ? and a.disabled = 0";
		int result = this.daoSupport.queryForInt(sql, DateUtil.getDateline(), gift_id);
		result = result > 0 ? 1 : 0;
		return result;
	}
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void delete(Integer gift_id) {
		String sql = "update es_full_discount_gift set disabled = 1 where gift_id = ?";
		this.daoSupport.execute(sql, gift_id);
		
		//将已经结束的促销活动关联的赠品信息去除
		sql = "update es_full_discount set is_send_gift = 0,gift_id = null where gift_id = ?";
		this.daoSupport.execute(sql, gift_id);
	}
}
