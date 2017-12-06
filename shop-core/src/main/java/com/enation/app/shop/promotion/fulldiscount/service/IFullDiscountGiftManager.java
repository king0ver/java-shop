package com.enation.app.shop.promotion.fulldiscount.service;

import java.util.List;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.framework.database.Page;

/**
 * 
 * 满优惠促销赠品管理接口
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午3:45:44
 */
public interface IFullDiscountGiftManager {
	/**
	 * 获取店铺促销活动赠品分页列表
	 * @param keyword	搜索关键字
	 * @param store_id	店铺ID
	 * @param pageNo	页数
	 * @param pageSize	每页记录数
	 * @return
	 */
	public Page list(String keyword, Integer shop, Integer pageNo, Integer pageSize);
	
	/**
	 * 添加店铺促销活动赠品
	 * @param StoreActivityGift
	 */
	public void add(FullDiscountGift fullDiscountGift);
	
	/**
	 * 修改店铺促销活动赠品信息
	 * @param StoreActivityGift
	 */
	public void edit(FullDiscountGift fullDiscountGift);
	
	/**
	 * 根据促销活动赠品ID获取店铺促销活动赠品信息
	 * @param gift_id 赠品ID
	 * @return
	 */
	public FullDiscountGift get(Integer gift_id);
	
	/**
	 * 获取店铺所有促销活动赠品集合
	 * @param store_id 店铺id
	 * @return
	 */
	public List<FullDiscountGift> listAll(Integer shop_id);
	
	/**
	 * 根据赠品ID判断赠品是否参与了促销活动(不包括已经结束的促销活动)
	 * @param gift_id 赠品ID
	 * @return result 0:未参与，1：已参与
	 */
	public int checkGiftInAct(Integer gift_id);
	
	/**
	 * 删除赠品信息
	 * @param gift_id 赠品ID
	 */
	public void delete(Integer gift_id);
}
