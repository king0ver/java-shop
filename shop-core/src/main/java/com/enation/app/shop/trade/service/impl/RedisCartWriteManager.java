package com.enation.app.shop.trade.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.promotion.bonus.model.MemberBonus;
import com.enation.app.shop.promotion.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.promotion.tool.service.IPromotionToolManager;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Consignee;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.model.vo.GroupPromotionVo;
import com.enation.app.shop.trade.model.vo.PriceProcessParam;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.ICartReadManager;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.support.OrderServiceConstant;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;

/**
 * 购物车Redis写入操作管理业务类
 * @author xulipeng
 * @version 1.0
 * @since v6.4
 * 2017年09月13日18:30:46
 */
@Service("cartWriteManager")
public class RedisCartWriteManager extends RedisCartManager implements ICartWriteManager {
	
	
//	@Autowired
//	private ICache cache;
	@Autowired
	private ICache cache;
	
	@Autowired
	private IPromotionToolManager promotionToolManager;
	
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	
	@Autowired
	private ICartReadManager cartReadManager;
	
	
	@Override
	public Product add(Integer skuid, Integer num, Integer activity_id) {
		Assert.notNull(skuid, "参数productid不能为空");
		Assert.notNull(num, "参数num不能为空");
		
		String cacheKey = getSessionKey();
		
		//1、先读取出此用户的所有购物项
		List<CartVo> cartList = this.cartReadManager.getCartlist();
		
		//2、读取这个产品的信息
		Product product  = this.getProduct(skuid,num);
		
		//3、读取这个产品参与的所有活动
		this.getPromotion(product, activity_id);
		
		//商品的卖家信息
		int sellerid =product.getSeller_id();
		String sellerName=product.getSeller_name();
		
		//查找出此货品的卖家是否已经加入到购物车中
		CartVo cart = this.findCart(sellerid, cartList);

		//此卖家还没有在购物列表中，new一个新的
		if (cart == null) {
			cart = new CartVo(sellerid,sellerName);
			
			//加入此商品
			product.setNum(num);
			product.setSubtotal(CurrencyUtil.mul(product.getOriginal_price(), num));
			cart.getProductList().add(product);
			
			//加入购物车列表
			cartList.add(cart);
		} else {
			
			this.updateProductNum(product, num, cart,false);
		}
		
		//调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		
		//组合展示购物车数据
		CartVo cartVo = this.copyProductToPromotion(cart);
		cartList.remove(cart);
		cartList.add(cartVo);
		
		this.putCache(cacheKey, cartList);
		
		return product;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#updateNum(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Product updateNum(Integer productid, Integer num) {
		Assert.notNull(productid, "参数productid不能为空");
		Assert.notNull(num, "参数num不能为空");
		
		String cacheKey = getSessionKey();
		
		//先获取此产品,并检测库存
		Product product  = this.getProduct(productid,num);
		
		// 先读取出此用户的所有购物项
		List<CartVo> cartList = this.cartReadManager.getCartlist();

		// 查找出此货品此用户是否已经加入到购物车中
		CartVo cart = this.findCart(product.getSeller_id(), cartList);	
		if(cart!=null){
			this.updateProductNum(product, num, cart,true);
		}
		
		//初始化所有商品的价格
		for (Product sku : cart.getProductList()) {
			//初始化商品购买时价格
			sku.setPurchase_price(sku.getOriginal_price());
			//初始化小计价格
			sku.setSubtotal(CurrencyUtil.mul(sku.getPurchase_price(), sku.getNum()));
		}

		// 调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		
		//组合展示购物车数据
		CartVo cartVo = this.copyProductToPromotion(cart);
		
		//将数据插入到原来list中的位置
		int index = cartList.indexOf(cart);
		cartList.remove(cart);
		cartList.add(index,cartVo);
		
		//重新压入内存
		this.putCache(cacheKey, cartList);

		return product;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#checked(java.lang.Integer, int)
	 */
	@Override
	public Product checked(Integer[] productids, int checked) {
		Assert.notNull(productids, "参数productid不能为空");
		
		//不合法的参数，忽略掉
		if(checked!=1 && checked!=0){
			return new Product();  
		}
		
		String cacheKey = getSessionKey();
		
		// 先读取出此用户的所有购物项
		List<CartVo> cartList = this.cartReadManager.getCartlist( );
		Product product =null;
		
		for (int i = 0; i < productids.length; i++) {
			
			for (Cart cart : cartList) {
				List<Product> productList  = cart.getProductList();
				product  = this.findProduct(productids[i], productList);
				if(product!=null){
					product.setIs_check(checked);
					break;
				}
			}
		}
		//调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		
		//重新渲染购物车列表，以便重新整理促销信息
		cartList = reRender(cartList);
		
		//重新压入缓存
		this.putCache(cacheKey, cartList);
		return product;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#checkedSellerAll(java.lang.Integer, int)
	 */
	@Override
	public Cart checkedSellerAll(Integer sellerid, int checked) {
		//不合法的参数，忽略掉
		if(checked!=1 && checked!=0){
			return null;
		}
		String cacheKey = getSessionKey();
		
		// 先读取出此用户的所有购物项
		List<CartVo> cartList = this.cartReadManager.getCartlist( );
		CartVo cart  = this.findCart(sellerid, cartList);
		List<Product> productList  = cart.getProductList();
		for (Product product : productList) {
			product.setIs_check(checked);
		}
	
		List<GroupPromotionVo> list = cart.getPromotionList();
		for (GroupPromotionVo groupPromotionVo : list) {
			List<Product> pList = groupPromotionVo.getProductList();
			
			for (Product product : pList) {
				product.setIs_check(checked);
			}
		}
		
		//调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		
		//重新渲染购物车列表，以便重新整理促销信息
		cartList = reRender(cartList);
		
		//重新压入缓存
		this.putCache(cacheKey, cartList);
		return cart;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#checkedAll(int)
	 */
	@Override
	public void checkedAll(int checked) {
		//不合法的参数，忽略掉
		if(checked!=1 && checked!=0){
			return ;
		}
		
		String cacheKey = getSessionKey();
		
		// 先读取出此用户的所有购物项
		List<CartVo> cartList = this.cartReadManager.getCartlist();

		for (CartVo cart : cartList) {
			List<Product> productList  = cart.getProductList();
			for (Product product : productList) {
				product.setIs_check(checked);
			}
		}
		
		//调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		
		//重新渲染购物车列表，以便重新整理促销信息
		cartList = reRender(cartList);
		
		//重新压入缓存
		this.putCache(cacheKey, cartList);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#delete(java.lang.Integer[])
	 */
	@Override
	public void delete(Integer[] productids) {

		Assert.notNull(productids, "参数productids不能为空");
		
		String cacheKey = getSessionKey();
		
		// 先读取出此用户的所有购物项
		List<CartVo> cartList = this.cartReadManager.getCartlist();

		//需要删除的购物车列表
		List<Cart> needRmove = new ArrayList<Cart>();		
		
		for (Cart cart : cartList) {
			List<Product> productList = cart.getProductList();
			
			// 循环产品id进行删除
			for (Integer productid : productids) {
				Product product = this.findProduct(productid, productList);
				if(product!=null){
					productList.remove(product);
					//如果已经没有产品了，需要移除掉此购物车
					if(productList.isEmpty()){
						needRmove.add(cart);
					}
				}
			}

		}
		if(!needRmove.isEmpty()){
			//删除空 Cart
			cartList.removeAll(needRmove);
		}

		//重新渲染购物车列表，以便重新整理促销信息
		cartList = reRender(cartList);
		
		//调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		//重新压入内存
		this.putCache(cacheKey, cartList);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#clean()
	 */
	@Override
	public void clean() {
		String cacheKey = getSessionKey();
		this.cache.remove(cacheKey);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#userCoupon(int, int)
	 */
	@Override
	public Cart userCoupon(int couponid, int sellerid) {

		List<CartVo> cartList  = this.cartReadManager.getCartlist();
		CartVo cartVo  = this.findCart(sellerid, cartList);
		
		Member member = UserConext.getCurrentMember();
		
		//读取此购物车的总价。
		//需要考虑的是如果是切换优惠券时，之前使用的优惠券优惠金额已经在总价中减去。在下面需要加上切换之前的优惠券金额
		Double total_price = cartVo.getPrice().getTotal_price();
		
		Coupon coupon =null;
		
		//使用或者取消使用
		int is_use = 0;
		if(couponid!=0){
			//读取使用的优惠券信息
			MemberBonus memberBonus =  this.b2b2cBonusManager.getOneMyBonus(member.getMember_id(), sellerid, couponid);
			
			//商品总价需大于优惠券的最低使用金额限制
			if(total_price<memberBonus.getMin_goods_amount()){
				throw new RuntimeException("未达到优惠券使用最低限额");
			}
			coupon = this.setCouponParam(memberBonus, sellerid);
			is_use=1;
		}
		
		this.promotionToolManager.countCouponPrice(cartVo, coupon, is_use);
		
		//重新压入内存
		this.putCache(getSessionKey(), cartList);
		return cartVo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#putCache(java.lang.String, java.util.List)
	 */
	@Override
	public void putCache(String cacheKey, List<CartVo> itemList) {
		//重新压入缓存
		cache.put(cacheKey, itemList);
	}


	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#setPromotion(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public void setPromotion(Integer sellerid,Integer skuid, Integer activity_id, String promotion_type) {
		
		//读取所有的购物车
		List<CartVo> cartList = this.cartReadManager.getCartlist();
		
		//读取当前店铺
		Cart cart = this.findCart(sellerid, cartList);
			
		//读取所有的产品集合
		List<Product> productList = cart.getProductList();
		
		//初始化所有商品的价格
		for (Product product : productList) {
			//初始化商品购买时价格
			product.setPurchase_price(product.getOriginal_price());
			//初始化小计价格
			product.setSubtotal(CurrencyUtil.mul(product.getPurchase_price(), product.getNum()));
		}
		
		//找到对应的产品对象
		Product product = this.findProduct(skuid, productList);
		
		//读取这个产品参与的所有活动
		List<CartPromotionGoodsVo> list = product.getSingle_list();
		
		//遍历所有的活动
		for (CartPromotionGoodsVo cartPromotionGoodsVo : list) {
			
			//将所选的促销活动设置为默认
			if(cartPromotionGoodsVo.getPromotion_type().equals(promotion_type) && cartPromotionGoodsVo.getActivity_id().equals(activity_id) ){
				cartPromotionGoodsVo.setIs_check(1);
			}else{
				cartPromotionGoodsVo.setIs_check(0);
			}
		}
		
		// 调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		
		//组合展示购物车数据
		CartVo cartVo = this.copyProductToPromotion(cart);
		
		//将数据插入到原来list中的位置
		int index = cartList.indexOf(cart);
		cartList.remove(cart);
		cartList.add(index,cartVo);
		
		//重新压入内存
		this.putCache(getSessionKey(), cartList);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#setShipping()
	 */
	@Override
	public void setShipping() {
		//读取所有的购物车
		List<CartVo> cartList = this.cartReadManager.getCartlist();
		//设置配送方式及价格
		this.promotionToolManager.setShippingPrice(cartList);
		//计算价格
		this.promotionToolManager.countShipingPrice(cartList);
		//重新压入内存
		this.putCache(getSessionKey(), cartList);
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#cleanChecked()
	 */
	@Override
	public void cleanChecked() {
		//原购物车集合
		List<CartVo> cartList  = this.cartReadManager.getCartlist();
		//新购物车集合
		List<CartVo> newCartList = new ArrayList<CartVo>();
		
		//遍历原购物车
		for (Cart cart : cartList) {
			//原商品集合
			List<Product> productList  = cart.getProductList();
			//新商品集合
			List<Product> newProductList  = new ArrayList<Product>();
			
			//遍历原商品集合
			for (Product product : productList) {
				//将没有选中下单的商品转移到 新商品集合 中 
				if(product.getIs_check()!=1){
					newProductList.add(product);
				}
			}
			
			//如果商品集合不为空,添加到 新购物车集合 中
			if(!newProductList.isEmpty()){
				cart.setProductList(newProductList);
				//组合展示购物车数据
				CartVo cartVo = this.copyProductToPromotion(cart);
				newCartList.add(cartVo);
			}
		}
		//将新的购物车集合压入缓存中
		this.putCache(this.getSessionKey(), newCartList);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#refreshCart()
	 */
	@Override
	public void refreshCart() {
		//原购物车集合
		List<CartVo> cartList  = this.cartReadManager.getCartlist();
		// 调用促销价格计算的接口
		this.promotionToolManager.countPrice(cartList);
		//将新的购物车集合压入缓存中
		this.putCache(this.getSessionKey(), cartList);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartWriteManager#mergeCart()
	 */
	@Override
	public void mergeCart() {
		//用户未登录时的 购物车key
		List<CartVo> noLoginCartList =  this.cartReadManager.getCartlistByNoLogin();
		
		for (CartVo cartVo : noLoginCartList) {
			List<Product> noLoginProductList = cartVo.getProductList();
			
			for (Product product : noLoginProductList) {
				Integer skuid = product.getProduct_id();
				Integer num = product.getNum();
				Integer activity_id = 0;
				
				//读取默认的活动id
				List<CartPromotionGoodsVo> singleList = product.getSingle_list();
				for (CartPromotionGoodsVo cartPromotionGoodsVo : singleList) {
					if(cartPromotionGoodsVo.getIs_check().intValue()==1){
						activity_id = cartPromotionGoodsVo.getActivity_id();
					}
				}
				
				this.add(skuid, num, activity_id);
			}
		}
		
		//清楚登录前的购物车数据
		cache.remove(OrderServiceConstant.CART_MEMBER_ID_PREFIX+ThreadContextHolder.getSession().getId());
		cache.remove(OrderServiceConstant.PRICE_SESSION_ID_PREFIX+ThreadContextHolder.getSession().getId());
	}
	
}
