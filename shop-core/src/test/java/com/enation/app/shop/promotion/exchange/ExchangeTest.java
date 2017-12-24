package com.enation.app.shop.promotion.exchange;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.service.IPromotionToolManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.framework.test.SpringTestSupport;

public class ExchangeTest extends SpringTestSupport{
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private IPromotionToolManager promotionToolManager;
	PromotionServiceConstant constant = new PromotionServiceConstant();
	@Test
	public void testCart() {
		// 登录会员
				memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(15);
		Product product = new Product();
		product.setNum(2);
		//商品成交价
		product.setPurchase_price(100);
		//商品原价
		product.setOriginal_price(1000);
		// 创建促销
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(78);
		cpgv.setPromotion_type(PromotionTypeEnum.EXCHANGE.getType());
		cpgs.add(cpgv);

		product.setSingle_list(cpgs);

		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);
		
		// 价格计算
		promotionToolManager.countPrice(carts);

//		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue().get(UserConext.getCurrentMember().getMember_id() + "_price_detail");
//		
//		Assert.assertEquals(detail.getDiscount_price(),1800 ,0);
//		Assert.assertEquals(detail.getExchange_point(),400,0);
		//System.out.println(detail);
	}

}
