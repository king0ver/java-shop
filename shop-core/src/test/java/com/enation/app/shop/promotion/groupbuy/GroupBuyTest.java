package com.enation.app.shop.promotion.groupbuy;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.groupbuy.model.vo.GroupBuyVo;
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
 * 团购测试类
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.4 2017年8月30日 上午10:42:50
 *
 */
public class GroupBuyTest extends SpringTestSupport {

	@Autowired
	private IPromotionToolManager promotionToolManager;
	@Autowired
	private IMemberManager memberManager;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	PromotionServiceConstant constant = new PromotionServiceConstant();

	@Test
	public void testCart() {

		// 促销活动
		GroupBuyVo gbv = new GroupBuyVo();
		gbv.setPrice(900);
		gbv.setOriginal_price(1000);
		redisTemplate.opsForValue().set(constant.getGroupbuyKey(1, 1), gbv);

		// 登录会员
		memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(1);
		Product product = new Product();
		product.setNum(10);
		product.setOriginal_price(1000);
		product.setPurchase_price(998);

		// 创建促销
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(1);
		cpgv.setPromotion_type(PromotionTypeEnum.GROUPBUY.getType());
		cpgs.add(cpgv);

		product.setSingle_list(cpgs);

		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);

		// 价格计算
		promotionToolManager.countPrice(carts);

		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");

		Assert.assertEquals(detail.getTotal_price(), 9000,9000);
		Assert.assertEquals(detail.getGoods_price(), 10000,10000); 
		System.out.println(detail);

	}

}
