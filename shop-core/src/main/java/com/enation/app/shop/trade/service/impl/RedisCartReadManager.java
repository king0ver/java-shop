package com.enation.app.shop.trade.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.ICartReadManager;
import com.enation.app.shop.trade.support.OrderServiceConstant;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 购物车Redis查询管理业务类
 * @author xulipeng
 * @version 1.0
 * @since v6.4
 * 2017年09月13日18:30:46
 */
@Service("cartReadManager")
public class RedisCartReadManager extends RedisCartManager implements ICartReadManager {

	@Autowired
	private ICache cache;
	
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartReadManager#getCartlist()
	 */
	@Override
	public List<CartVo> getCartlist() {
		String cacheKey = this.getSessionKey();
		
		List<CartVo> itemList = (List<CartVo>) cache.get(cacheKey);
		// 如果为空new一个 list返回
		if (itemList == null) {
			itemList = new ArrayList();
		}
		
		//设置店铺全选状态
		for (CartVo cartVo : itemList) {
			//设置默认为店铺商品全选
			cartVo.setChecked(1);
			List<Product> productList = cartVo.getProductList();
			for (Product product : productList) {
				if(product.getIs_check()==0){
					cartVo.setChecked(0);
					break;
				}
			}
		}
		
		return itemList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartReadManager#count()
	 */
	@Override
	public Integer count() {
		List<CartVo> cartList  = this.getCheckedItems();
		int count =0;
		for (Cart cart : cartList) {
			List<Product> productList = cart.getProductList();
			for (Product product : productList) {
				int num =product.getNum();
				count+=num;
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartReadManager#getCart(java.lang.Integer)
	 */
	@Override
	public CartVo getCart(Integer ownerid) {
		String cacheKey = this.getSessionKey();
		List<CartVo> itemList = (List<CartVo>) cache.get(cacheKey);
		return this.findCart(ownerid, itemList);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartReadManager#getCheckedItems()
	 */
	@Override
	public List<CartVo> getCheckedItems() {
		List<CartVo> cartList  = this.getCartlist();
		for (Cart cart : cartList) {
			List<Product> productList  = cart.getProductList();
			List<Product> newProductList  = new ArrayList<Product>();
			  
			  for (Product product : productList) {
				//只将选中的压入  
				if(product.getIs_check()==1){
					newProductList.add(product);
				}
			}
			cart.setProductList(newProductList);
		}
		
		//重新渲染购物车列表，以便重新整理促销信息
		cartList = reRender(cartList);
		
		return cartList;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.trade.service.ICartReadManager#getCartlistByNoLogin()
	 */
	@Override
	public List<CartVo> getCartlistByNoLogin() {
		String cacheKey = OrderServiceConstant.CART_MEMBER_ID_PREFIX+ThreadContextHolder.getSession().getId();
		List<CartVo> itemList = (List<CartVo>) cache.get(cacheKey);
		// 如果为空new一个 list返回
		if (itemList == null) {
			itemList = new ArrayList();
		}
		return itemList;
	}
	
}
