package com.enation.app.shop.promotion.fulldiscount;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
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
 * 
 * 满优惠促销活动单元测试
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月6日 下午4:59:56
 */
public class FullDiscountTest extends SpringTestSupport{

	@Autowired
	private IMemberManager memberManager;

	@Autowired
	private IPromotionToolManager promotionToolManager;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	PromotionServiceConstant constant = new PromotionServiceConstant();
	/**
	 * 满减测试
	 */
	@Test
	public void testCart() {
		
		//促销活动
		FullDiscountVo fdv = new FullDiscountVo();
		fdv.setMinus_value(10.0);
		fdv.setIs_full_minus(1);
		fdv.setFull_money(99.0);
		redisTemplate.opsForValue().set(constant.getFullDiscountKey(1), fdv);
		
		// 登录会员
		memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(1);
		Product product = new Product();
		product.setNum(1);
		//原价
		product.setOriginal_price(100);
		//购买时的成交价
		product.setPurchase_price(100);
		product.setSubtotal(100);
		
		// 创建促销
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(1);
		cpgv.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
		cpgs.add(cpgv);

		product.setGroup_list(cpgs);
		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);
		
		// 价格计算
		promotionToolManager.countPrice(carts);
		
		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");

		Assert.assertEquals(detail.getDiscount_price(), 10,10);
		Assert.assertEquals(detail.getTotal_price(), 90,90);
		System.out.println("_____________________________");
		System.out.println(detail.toString());
		System.out.println("_____________________________");

		
	}
	/**
	 * 满折测试
	 */
	@Test
	public void testCart2() {
		
		//促销活动
		FullDiscountVo fdv = new FullDiscountVo();
		fdv.setDiscount_value(8.0);
		fdv.setIs_discount(1);
		fdv.setFull_money(99.0);
		redisTemplate.opsForValue().set(constant.getFullDiscountKey( 1), fdv);
				
		// 登录会员
		memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(1);
		Product product = new Product();
		product.setNum(1);
		//原价
		product.setOriginal_price(100);
		//购买时的成交价
		product.setPurchase_price(100);
		product.setSubtotal(100);
		
		// 创建促销
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(1);
		cpgv.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
		cpgs.add(cpgv);

		product.setGroup_list(cpgs);
		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);
		
		// 价格计算
		promotionToolManager.countPrice(carts);
		
		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");

		Assert.assertEquals(detail.getDiscount_price(), 20,20);
		Assert.assertEquals(detail.getTotal_price(), 80,80);
		System.out.println("_____________________________");
		System.out.println(detail.toString());
		System.out.println("_____________________________");

		
	}
	/**
	 * 免邮费测试
	 */
	@Test
	public void testCart3() {
		
		//促销活动
		FullDiscountVo fdv = new FullDiscountVo();
		fdv.setFull_money(99.0);
		fdv.setIs_free_ship(1);
		redisTemplate.opsForValue().set(constant.getFullDiscountKey(1), fdv);
				
		// 登录会员
		memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(1);
		Product product = new Product();
		product.setNum(1);
		//原价
		product.setOriginal_price(100);
		//购买时的成交价
		product.setPurchase_price(100);
		product.setSubtotal(100);
		
		// 创建促销
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(1);
		cpgv.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
		cpgs.add(cpgv);

		product.setGroup_list(cpgs);
		List<Product> products = new ArrayList<>();
		products.add(product);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);
		
		// 价格计算
		promotionToolManager.countPrice(carts);
		
		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");

		Assert.assertEquals(detail.getDiscount_price(), 0,0);
		Assert.assertEquals(detail.getTotal_price(), 100,100);
		System.out.println("_____________________________");
		System.out.println(detail.toString());
		System.out.println("_____________________________");

		
	}
	
	/**
	 * 多活动测试
	 */
	@Test
	public void testCart4() {
		
		//促销活动
		FullDiscountVo fdv = new FullDiscountVo();
		fdv.setMinus_value(10.0);
		fdv.setIs_full_minus(1);
		fdv.setFull_money(99.0);
		redisTemplate.opsForValue().set(constant.getFullDiscountKey(1), fdv);
		
		FullDiscountVo fdv1 = new FullDiscountVo();
		fdv1.setDiscount_value(8.0);
		fdv1.setIs_discount(1);
		fdv1.setFull_money(99.0);
		redisTemplate.opsForValue().set(constant.getFullDiscountKey(1), fdv1);
			
		// 登录会员
		memberManager.login("food");
		// 购物车
		CartVo cart = new CartVo();
		cart.setPrice(new PriceDetail());
		cart.setSeller_id(1);
		Product product = new Product();
		product.setNum(1);
		//原价
		product.setOriginal_price(100);
		//购买时的成交价
		product.setPurchase_price(100);
		product.setSubtotal(100);
		
		Product product1 = new Product();
		product1.setNum(1);
		//原价
		product1.setOriginal_price(100);
		//购买时的成交价
		product1.setPurchase_price(100);
		product1.setSubtotal(100);
		
		// 创建促销
		List<CartPromotionGoodsVo> cpgs = new ArrayList<>();
		CartPromotionGoodsVo cpgv = new CartPromotionGoodsVo();
		cpgv.setActivity_id(1);
		cpgv.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
		cpgs.add(cpgv);
		
		List<CartPromotionGoodsVo> cpgs1 = new ArrayList<>();
		CartPromotionGoodsVo cpgv1 = new CartPromotionGoodsVo();
		cpgv1.setActivity_id(2);
		cpgv1.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
		cpgs1.add(cpgv1);

		product.setGroup_list(cpgs);
		product1.setGroup_list(cpgs1);
		
		List<Product> products = new ArrayList<>();
		products.add(product);
		products.add(product1);
		cart.setProductList(products);

		List<CartVo> carts = new ArrayList<>();
		carts.add(cart);
		
		// 价格计算
		promotionToolManager.countPrice(carts);
		
		PriceDetail detail = (PriceDetail) redisTemplate.opsForValue()
				.get(UserConext.getCurrentMember().getMember_id() + "_price_detail");

		Assert.assertEquals(detail.getDiscount_price(), 30,30);
		Assert.assertEquals(detail.getTotal_price(), 170,170);
		System.out.println("_____________________________");
		System.out.println(detail.toString());
		System.out.println("_____________________________");

		
	}
}
