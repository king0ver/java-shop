package com.enation.app.shop.promotion.halfprice.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.service.impl.GoodsManager;
import com.enation.app.shop.promotion.halfprice.model.po.HalfPrice;
import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 第二次半价促销实现类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月20日 下午5:06:30
 */
@Service("halfPriceManager")
public class HalfPriceManager implements IHalfPriceManager{

	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;
	@Autowired
	private HalfPriceManager halfPriceManager;
	@Autowired
	private ISellerManager storeMemberManager;
	@Autowired
	private ICache cache;
	@Autowired
	private GoodsManager goodsManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager#add(com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(HalfPriceVo halfPriceVo) {
		try {
			/**将活动信息存入数据库*/
			HalfPrice halfPrice = new HalfPrice();
			BeanUtils.copyProperties(halfPrice, halfPriceVo);
			this.daoSupport.insert("es_half_price", halfPrice);
			Integer fd_id = this.daoSupport.getLastId("es_half_price");
			/** 获取当前添加第二件半价活动的活动id */
			Integer activity_id = this.daoSupport.getLastId("es_half_price");
	
			List<PromotionGoodsVo> promotionGoodsVoList = new ArrayList<PromotionGoodsVo>();
			/** 填充已经添加的活动ID */
			promotionGoodsVoList = halfPriceVo.getGoodsList();
			for(PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
				promotionGoodsVo.setActivity_id(activity_id);
			}
			/** 添加商品对照表 */
			promotionGoodsManager.add(promotionGoodsVoList);
			/** 填充halfPriceVo */
			halfPriceVo.setHp_id(activity_id);
			halfPriceVo.setGoodsList(promotionGoodsVoList);
			if(halfPriceVo!= null) {
				/** 获取第二次半价活动key */
				String plugin = PromotionServiceConstant.getHalfPriceKey( activity_id);
				
				/** 写入reids */
				cache.put(plugin, halfPriceVo);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.service.IHalfPriceManager#get(java.lang.Integer)
	 */
	@Override
	public HalfPriceVo get(Integer hp_id) {
		String plugin = PromotionServiceConstant.getHalfPriceKey(hp_id);
		HalfPriceVo halfPriceVo = (HalfPriceVo) cache.get(plugin);
		if(halfPriceVo == null) {
			String sql = "select * from es_half_price where hp_id = ? ";
			halfPriceVo = this.daoSupport.queryForObject(sql, HalfPriceVo.class, hp_id);
		}
		return halfPriceVo;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager#edit(com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(HalfPriceVo halfPriceVo) {
		try {
			HalfPrice halfPrice = new HalfPrice();
			BeanUtils.copyProperties(halfPrice, halfPriceVo);
			/** 更新第二件半价活动表 */
			String sql = " update es_half_price set title = ?,start_time = ?,end_time = ?,range_type = ?,description = ? where hp_id = ? ";		 
			this.daoSupport.execute(sql,halfPrice.getTitle(),halfPrice.getStart_time(),halfPrice.getEnd_time(),
			halfPrice.getRange_type(),halfPrice.getDescription(),halfPriceVo.getHp_id());
			
			
			List<PromotionGoodsVo> promotionGoodsVoList = halfPriceVo.getGoodsList();
			/** 填充修改的活动ID */
			for(PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
				promotionGoodsVo.setActivity_id(halfPriceVo.getHp_id());
			}
			/** 删除商品对照表的数据 */
			promotionGoodsManager.delete(halfPriceVo.getHp_id(),PromotionTypeEnum.HALFPRICE.getType());
			/** 再重新添加商品对照表 */
			promotionGoodsManager.add(promotionGoodsVoList);
			if(halfPriceVo!= null) {
				/** 获取第二次半价活动key */
				String plugin = PromotionServiceConstant.getHalfPriceKey(halfPrice.getHp_id());
				/** 写入reids */
				cache.put(plugin, halfPriceVo);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager#delete(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer activity_id) {
		
		String sql = "update es_half_price set disabled = 1 where hp_id = ?";
		this.daoSupport.execute(sql, activity_id);
		/** s */
		promotionGoodsManager.delete(activity_id, PromotionTypeEnum.HALFPRICE.getType());
		HalfPriceVo halfPriceVo  = this.get(activity_id);
		if(halfPriceVo!= null) {
			/** 获取第二次半价活动key */
			String plugin = PromotionServiceConstant.getHalfPriceKey(halfPriceVo.getHp_id());
			/** redis删除缓存 */
			cache.remove(plugin);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager#HalfMap(com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo)
	 */
	@Override
	public List<PromotionGoodsVo> HalfMap(HalfPriceVo halfPriceVo) {
		List<PromotionGoodsVo> goodsList = halfPriceVo.getGoodsList();
		/**存放放入session中的商品集合*/
		List<PromotionGoodsVo> list = new ArrayList<>();
		if(!goodsList.isEmpty()) {
			StringBuffer goods_id = new StringBuffer() ;
			for(int i =0;i<goodsList.size();i++) {
				goods_id.append(goodsList.get(i).getGoods_id().toString());
				if(i<goodsList.size()-1) {
					goods_id.append(",");
				}
			}
			StringBuffer product_id = new StringBuffer() ;
			for(int i =0;i<goodsList.size();i++) {
				product_id.append(goodsList.get(i).getProduct_id().toString());
				if(i<goodsList.size()-1) {
					product_id.append(",");
				}
			}
			/** 查询冲突的活动 */
			String sql = "select * from es_promotion_goods  where promotion_type = ? and ((start_time <= ? and end_time >= ?) or (start_time <= ? and end_time >= ?) or (start_time>= ? and end_time <= ?))"
					+ " and goods_id in("+goods_id+") and product_id in("+product_id+")";
			if(halfPriceVo.getHp_id() != null && halfPriceVo.getHp_id() != 0){
				sql += " and activity_id != "+halfPriceVo.getHp_id()+"";
			}
			List<PromotionGoodsVo> confilstList = this.daoSupport.queryForList(sql,PromotionGoodsVo.class,PromotionTypeEnum.HALFPRICE.getType(),
					halfPriceVo.getStart_time(),halfPriceVo.getStart_time(),halfPriceVo.getEnd_time(),halfPriceVo.getEnd_time(),halfPriceVo.getStart_time(),halfPriceVo.getEnd_time());
			
			
			if(confilstList.size()>=0) {
				List<PromotionGoodsVo> promotionGoodsVoList = halfPriceVo.getGoodsList();
				List<PromotionGoodsVo> promotionList = new ArrayList<PromotionGoodsVo>();
				for(PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
					for(PromotionGoodsVo confilstPromotion :confilstList) {
						if(promotionGoodsVo.getProduct_id().equals(confilstPromotion.getProduct_id())) {
							confilstPromotion.setName(promotionGoodsVo.getName());
							confilstPromotion.setThumbnail(promotionGoodsVo.getThumbnail());
							list.add(confilstPromotion);
						}
					}
				}
			}
		}
		return list;
}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager#getHalfPriceList(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page getHalfPriceList(String keyword, Integer shop_id, Integer pageNo, Integer pageSize) {
		String sql = "select * from es_half_price where shop_id = ? and disabled = 0";
		
		//如果关键字不为空
		if(keyword != null && !StringUtil.isEmpty(keyword)){
			sql += " and title like '%" + keyword + "%'";
		}
		
		sql += " order by hp_id desc";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, shop_id);
		List list = (List) page.getResult();
		Map map = new HashMap();
		
		//遍历获取的当前页的记录
		for(int i = 0; i < list.size(); i++){
			map = (Map) list.get(i);
			String statusStr = this.getCurrentStatus((Long)(map.get("start_time")), (Long)(map.get("end_time")));
            map.put("status", statusStr);
		}
		
		return page;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager#getCurrentStatus(java.lang.Long, java.lang.Long)
	 */
	@Override
    public String getCurrentStatus(Long startTime, Long endTime) {
        String status = "";
        long currentTime = System.currentTimeMillis() / 1000; //获取当前时间秒数
        if (startTime > currentTime) {
            status = "未开始";
        } else if ((startTime <= currentTime) && (endTime >= currentTime)) {
            status = "进行中";
        } else if (endTime < currentTime) {
            status = "已结束";
        }
        return status;
    }
	@Override
	public HalfPriceVo getFromDB(Integer hp_id) {
		String sql = "select * from es_half_price where hp_id = ?";
		HalfPriceVo halfPriceVo = this.daoSupport.queryForObject(sql, HalfPriceVo.class, hp_id);
		return halfPriceVo;
	}
	
	
	
}
