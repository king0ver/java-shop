package com.enation.app.shop.promotion.bonus.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.promotion.bonus.model.Bonus;
import com.enation.app.shop.promotion.bonus.model.StoreBonus;
import com.enation.app.shop.promotion.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.service.ICartReadManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.JsonResultUtil;

import freemarker.template.TemplateModelException;

/**
 * 结算页—我的优惠券列表(限B2b2c-wap端使用)
 * 描述：根据已选的购物车商品，查询我的优惠券中所有可用的优惠券集合
 * 返回格式：Map{totalNum:*,totalStoreBonus:List<StoreBonus>} 
 * <br>其中totalNum：所有店铺(可用或者不可用)优惠券数量,totalStoreBonus:所有店铺的(可用或者不可用)优惠券对象
 * @author xulipeng
 * @version v1.0
 * @since v6.2.1
 */
@Component
@Scope("prototype")
public class MyStoreBonusByWapAccountTag extends BaseFreeMarkerTag {

	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	
	@Autowired
	private ICartReadManager cartReadManager;
	
	/**
	 * @param	is_usable	1为可用，0为不可用。默认为1
	 * @return	返回所有店铺优惠券列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		Member member = UserConext.getCurrentMember();
		
		if(member ==null){
			return JsonResultUtil.getErrorJson("未登录，不能使用此api");
		}
		
		//判断 是否可用参数
		Integer is_usable = (Integer) params.get("is_usable");
		if(is_usable==null){
			is_usable=1;
		}
		
		//默认为1页
		int page =1;
		//默认查询的条数
		int pageSize = 100;
		
		//所有店铺优惠券的数量（计数）
		long totalNum=0;
		
		//所有店铺优惠券集合
		List<StoreBonus> totalStoreBonus = new ArrayList();
		
		//由session中获取已选中购物车列表
		List<CartVo> cartList = this.cartReadManager.getCheckedItems();
		
		//购物车没有商品或者进入过购物车界面
		if(cartList==null){
			return null;
		}
		
		for (CartVo cartVo : cartList) {
			
			// 获取此店铺id
			int store_id = cartVo.getSeller_id();
			
			// 获取店铺名称
			String store_name = cartVo.getSeller_name();
			
			PriceDetail detail = cartVo.getPrice();
			
			//查询此店铺的可用或者不可用优惠券数量
			Page webpage = this.b2b2cBonusManager.getMyBonusByIsUsable(page,pageSize, member.getMember_id(), is_usable, detail.getTotal_price(), store_id);
	
			Long totalCount = webpage.getTotalCount();
			List<Bonus> bonusList = (List) webpage.getResult();
			bonusList = bonusList == null ? new ArrayList() : bonusList;
			
			Coupon coupon = null;
			if(!cartVo.getCouponList().isEmpty()){
				//获得此店铺已使用的优惠券
				coupon = cartVo.getCouponList().get(0);
			}
			
			//判断是否为空 并且 查询的是可用优惠券
			if(coupon!=null && is_usable.intValue()==1){
				for (Bonus bonus : bonusList) {
					//如果相等 设为已使用的状态
					if(bonus.getBonus_id().equals(coupon.getCoupon_id())){
						bonus.setIs_used(1);
					}else{
						bonus.setIs_used(0);
					}
				}
			}
			
			//生成店铺优惠券对象
			StoreBonus storeBonus = new StoreBonus();
			storeBonus.setStore_id(store_id);
			storeBonus.setStore_name(store_name);
			storeBonus.setBonusList(bonusList);
			
			//所有店铺优惠券数量 计数
			totalNum += totalCount;
			totalStoreBonus.add(storeBonus);
		}
		
		Map map = new HashMap();
		map.put("totalNum", totalNum);
		map.put("totalStoreBonus", totalStoreBonus);
		
		return map;
	}

}
