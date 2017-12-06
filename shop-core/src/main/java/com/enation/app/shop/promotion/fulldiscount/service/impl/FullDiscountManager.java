package com.enation.app.shop.promotion.fulldiscount.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscount;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 
 * 满优惠活动管理类
 * 
 * @author zjp
 * @version v1.0
 * @since v6.4.0 2017年9月4日 下午5:26:07
 */
@Service
public class FullDiscountManager implements IFullDiscountManager {
	@Autowired
	IDaoSupport daoSupport;

	@Autowired
	ISellerManager sellerManager;

	@Autowired
	private ICache cache;

	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager#
	 * checkGoods(com.enation.app.shop.promotion.fulldiscount.model.vo.
	 * FullDiscountVo)
	 */
	@Override
	public Boolean checkGoods(FullDiscountVo fullDiscountVo) {

		List<PromotionGoodsVo> goodsList = fullDiscountVo.getGoodsList();
		StringBuffer goods_id = new StringBuffer();
		for (int i = 0; i < goodsList.size(); i++) {
			goods_id.append(goodsList.get(i).getGoods_id().toString());
			if (i < goodsList.size() - 1) {
				goods_id.append(",");
			}
		}
		/** 获取存在冲突的商品 */
		String sql = "select * from es_promotion_goods  where  promotion_type = ? and ((start_time <= ? and end_time >= ?) or (start_time <= ? and end_time >= ?) or (start_time>= ? and end_time <= ?)) and goods_id in ( "
				+ goods_id.toString() + ")";
		if (fullDiscountVo.getFd_id() != null && fullDiscountVo.getFd_id() != 0) {
			sql += " and activity_id != " + fullDiscountVo.getFd_id() + "";
		}
		List<PromotionGoodsVo> confilstList = this.daoSupport.queryForList(sql, PromotionGoodsVo.class,
				PromotionTypeEnum.FULLDISCOUNT.getType(), fullDiscountVo.getStart_time(),
				fullDiscountVo.getStart_time(), fullDiscountVo.getEnd_time(), fullDiscountVo.getEnd_time(),
				fullDiscountVo.getStart_time(), fullDiscountVo.getEnd_time());

		if (confilstList.size() == 0) {
			return false;
		}
		/** 存放放入session中的商品集合 */
		List<PromotionGoodsVo> list = new ArrayList<>();

		for (PromotionGoodsVo promotionGoodsVo : goodsList) {
			for (PromotionGoodsVo promotionGoods : confilstList) {
				if (promotionGoods.getGoods_id().equals(promotionGoodsVo.getGoods_id())) {
					promotionGoods.setName(promotionGoodsVo.getName());
					promotionGoods.setThumbnail(promotionGoodsVo.getThumbnail());
					list.add(promotionGoods);
				}
			}
		}
		/** 将冲突商品列表存入session */
		ThreadContextHolder.getSession().setAttribute("actList", list);
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager#get(
	 * java.lang.Integer)
	 */
	@Override
	public FullDiscountVo get(Integer fd_id) {

		String plugin = PromotionServiceConstant.getFullDiscountKey(fd_id);
		FullDiscountVo fullDiscountVo = (FullDiscountVo) cache.get(plugin);
		if (fullDiscountVo == null) {
			String sql = "select * from es_full_discount where fd_id = ? ";
			fullDiscountVo = this.daoSupport.queryForObject(sql, FullDiscountVo.class, fd_id);
			cache.put(plugin, fullDiscountVo);
		}
		return fullDiscountVo;
	}

	@Override
	public FullDiscountVo getFromDB(Integer fd_id) {
		String sql = "select * from es_full_discount where fd_id = ? ";
		FullDiscountVo fullDiscountVo = this.daoSupport.queryForObject(sql, FullDiscountVo.class, fd_id);
		return fullDiscountVo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager#add(
	 * com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(FullDiscountVo fullDiscountVo) {
		try {
			/** 将活动信息存入数据库 */
			FullDiscount fullDiscount = new FullDiscount();
			BeanUtils.copyProperties(fullDiscount, fullDiscountVo);
			this.daoSupport.insert("es_full_discount", fullDiscount);
			Integer fd_id = this.daoSupport.getLastId("es_full_discount");
			List<PromotionGoodsVo> promotionGoodsVoList = new ArrayList<PromotionGoodsVo>();

			promotionGoodsVoList = fullDiscountVo.getGoodsList();
			for (PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
				promotionGoodsVo.setActivity_id(fd_id);
			}

			promotionGoodsManager.add(promotionGoodsVoList);
			/** 填充fullDiscountVo */
			fullDiscountVo.setFd_id(fd_id);
			fullDiscountVo.setGoodsList(promotionGoodsVoList);
			/** 获取满优惠活动key */
			String plugin = PromotionServiceConstant.getFullDiscountKey(fd_id);

			/** 写入reids */
			cache.put(plugin, fullDiscountVo);

		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager#edit
	 * (com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(FullDiscountVo fullDiscountVo) {
		try {
			FullDiscount fullDiscount = new FullDiscount();
			BeanUtils.copyProperties(fullDiscount, fullDiscountVo);
			this.daoSupport.update("es_full_discount", fullDiscount, "fd_id = " + fullDiscount.getFd_id());

			List<PromotionGoodsVo> promotionGoodsVoList = fullDiscountVo.getGoodsList();
			for (PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
				promotionGoodsVo.setActivity_id(fullDiscount.getFd_id());
			}

			/** 清空活动商品表 */
			promotionGoodsManager.delete(fullDiscount.getFd_id(), PromotionTypeEnum.FULLDISCOUNT.getType());
			promotionGoodsManager.add(promotionGoodsVoList);
			/** 获取满优惠活动key */
			String plugin = PromotionServiceConstant.getFullDiscountKey(
					fullDiscount.getFd_id());

			/** 写入reids */
			cache.put(plugin, fullDiscountVo);

		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager#
	 * getHalfPriceList(java.lang.String, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer)
	 */
	@Override
	public Page getFullDiscountList(String keyword, Integer store_id, Integer pageNo, Integer pageSize) {
		String sql = "select * from es_full_discount where shop_id = ? and disabled = 0";

		// 如果关键字不为空
		if (keyword != null && !StringUtil.isEmpty(keyword)) {
			sql += " and title like '%" + keyword + "%'";
		}

		sql += " order by fd_id desc";

		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, store_id);

		List list = (List) page.getResult();
		Map map = new HashMap();

		// 遍历获取的当前页的记录
		for (int i = 0; i < list.size(); i++) {
			map = (Map) list.get(i);
			String statusStr = this.getCurrentStatus((Long) (map.get("start_time")), (Long) (map.get("end_time")));
			map.put("status", statusStr);
		}

		return page;
	}

	/**
	 * 获取当前状态
	 * 
	 * @param startTime
	 *            活动开始时间
	 * @param endTime
	 *            活动结束时间
	 * @return
	 */
	private String getCurrentStatus(Long startTime, Long endTime) {
		String status = "";
		long currentTime = System.currentTimeMillis() / 1000; // 获取当前时间秒数
		if (startTime > currentTime) {
			status = "未开始";
		} else if ((startTime <= currentTime) && (endTime >= currentTime)) {
			status = "进行中";
		} else if (endTime < currentTime) {
			status = "已结束";
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager#
	 * delete(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer fd_id) {
		String sql = "update es_full_discount set disabled = 1 where fd_id = ?";
		this.daoSupport.execute(sql, fd_id);
		promotionGoodsManager.delete(fd_id, PromotionTypeEnum.FULLDISCOUNT.getType());
		FullDiscountVo fullDiscountVo = this.get(fd_id);
		if (fullDiscountVo != null) {
			/** 获取满优惠活动key */
			String plugin = PromotionServiceConstant.getFullDiscountKey(fullDiscountVo.getFd_id());
			/** redis删除缓存 */
			cache.remove(plugin);
		}
	}

}
