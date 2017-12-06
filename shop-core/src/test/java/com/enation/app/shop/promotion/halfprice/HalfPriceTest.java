package com.enation.app.shop.promotion.halfprice;


import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.service.IPromotionToolManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.test.SpringTestSupport;
import junit.framework.Assert;

/**
 * 第二件半价促销活动单元测试
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年8月31日 下午2:58:12
 */
public class HalfPriceTest extends SpringTestSupport{

	@Autowired
	private IMemberManager memberManager;

	@Autowired
	private IPromotionToolManager promotionToolManager;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	PromotionServiceConstant constant = new PromotionServiceConstant();
	
	@Test
	public void testCart() {
		
		/** 登录会员 */
		memberManager.login("food");
		/** 购物车 */
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(1);
		Product product = new Product();
		/** 原价 */
		product.setOriginal_price(100);
		/** 购买时的成交价 */
		product.setPurchase_price(100);
		
		
//		/** 计算一件商品优惠价格 */
//		product.setNum(1);
		/** 计算倆件商品优惠价格 */
		product.setNum(2);
//		/** 计算三件商品优惠价格 */
//		product.setNum(3);
		
		
		
		/** 创建促销 */
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(1);
		cpgv.setPromotion_type(PromotionTypeEnum.HALFPRICE.getType());
		cpgs.add(cpgv);

		product.setSingle_list(cpgs);
		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);
		List<CartVo> cartsone = new ArrayList<>();
		cartsone.add(cart);
		/** 价格计算 */
		promotionToolManager.countPrice(cartsone);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);
		
		// 价格计算
		promotionToolManager.countPrice(carts);
		
		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");
		
		
		
//		/** 一件商品 */
//		Assert.assertEquals(detail.getDiscount_price(), 0,0);
//		Assert.assertEquals(detail.getTotal_price(), 100,100);
		/** 二件商品 */
		Assert.assertEquals(detail.getDiscount_price(), 50,50);
		Assert.assertEquals(detail.getTotal_price(), 150,150);
//		/** 三件商品 */
//		Assert.assertEquals(detail.getDiscount_price(), 50,50);
//		Assert.assertEquals(detail.getTotal_price(), 250,250);
		
		
		
		System.out.println("_____________________________");
		System.out.println(detail.toString());
		System.out.println("_____________________________");
		
		
	}

}
