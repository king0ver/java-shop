package com.enation.app.shop.promotion.minus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.service.IPromotionToolManager;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.test.SpringTestSupport;
import com.enation.framework.util.CurrencyUtil;

import junit.framework.Assert;

/**
 * 单品立减促销活动单元测试
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月1日下午3:28:36
 *
 */
public class MinusTest extends SpringTestSupport {

	@Autowired
	private IMemberManager memberManager;

	@Autowired
	private IPromotionToolManager promotionToolManager;

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void testCart() {

		this.memberManager.login("food");

		// 登录会员
		memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(15);
		cart.setSeller_name("food");
		Product product = new Product();
		product.setSeller_id(15);
		product.setSeller_name("food");
		product.setNum(2);
		// 原价
		product.setOriginal_price(25.9);
		// 购买时的成交价
		product.setPurchase_price(25.9);

		// 创建促销
		List<CartPromotionGoodsVo> CartPromotionGoodsVos = new ArrayList<CartPromotionGoodsVo>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setTitle("单品立减测试");
		cpgv.setActivity_id(5);
		cpgv.setPromotion_type(PromotionTypeEnum.MINUS.getType());
		CartPromotionGoodsVos.add(cpgv);

		product.setSingle_list(CartPromotionGoodsVos);
		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);

		// 价格计算
		this.promotionToolManager.countPrice(carts);
		System.out.println(product.getPurchase_price());

		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");

		if (detail != null) {
			Assert.assertEquals(detail.getDiscount_price(), 20.00, 20.00);
			Assert.assertEquals(detail.getTotal_price(), 31.80, 31.80);
			System.out.println("_____________________________");
			System.out.println(detail.toString());
			System.out.println("_____________________________");
		}

	}

}
