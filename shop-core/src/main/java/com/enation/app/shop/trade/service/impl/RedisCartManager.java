package com.enation.app.shop.trade.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.goods.model.po.GoodsGallery;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsGalleryManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.goods.service.IGoodsSkuManager;
import com.enation.app.shop.goods.service.impl.GoodsManager;
import com.enation.app.shop.promotion.bonus.model.MemberBonus;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartPromotionGoodsVo;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.model.vo.GroupPromotionVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.support.OrderServiceConstant;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
/**
 * 购物车Redis管理业务类
 * @author xulipeng
 * @version 1.0
 * @since v6.4
 * 2017年09月13日18:31:11
 */
public class RedisCartManager{
	
	@SuppressWarnings("rawtypes")

	@Autowired
	private ICache cache;
	
	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;
	
	@Autowired
	private IGoodsSkuManager goodsSkuManager;

	@Autowired
	private IGoodsGalleryManager goodsGalleryManager;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IFullDiscountManager fullDiscountManager;
	

	/**
	 * 读取商品sku，并检测库存
	 * @param goods_id 	商品id
	 * @param sku_id	skuid
	 * @param num		将要购买的数量
	 * @return
	 */
	protected Product getProduct(int sku_id,int num){
		
		GoodsSkuVo sku = this.goodsSkuManager.getSkuFromCache(sku_id);
		//判断是否为空
		if(sku == null ){
			throw new RuntimeException("此商品已下架，请移除后，重新加入购物车");
		}
		
		//判断可用库存是否为null || 可用库存是否为0
		if(sku.getEnable_quantity() == null || sku.getEnable_quantity()==0 || num > sku.getEnable_quantity()){
			throw new RuntimeException("商品库存不足");
		}
		
		// 生成一个购物项
		Product product = new Product();
		product.setSeller_id(sku.getSeller_id());
		product.setSeller_name(sku.getSeller_name());
		product.setGoods_id(sku.getGoods_id());
		product.setProduct_id(sku_id);
		product.setCat_id(sku.getCategory_id());
		product.setGoods_image(sku.getThumbnail());
		product.setName(sku.getGoods_name());
		product.setProduct_sn(sku.getSn());
		product.setPurchase_price(sku.getPrice());
		product.setOriginal_price(sku.getPrice());
		product.setSpecList(sku.getSpecList());
		product.setIs_free_freight(sku.getGoods_transfee_charge());
		product.setGoods_weight(sku.getWeight());
		
		return product;
	}
	
	
	/**
	 * 读取商品参加的所有活动
	 * @param product
	 * @param activity_id 默认参与的活动
	 * @return
	 */
	protected Product getPromotion(Product product,Integer activity_id){
		
		List<PromotionGoodsVo> list = promotionGoodsManager.getPromotion(product.getGoods_id());
		//单品活动集合
		List<CartPromotionGoodsVo> single_list = new ArrayList<>();
		//组合活动集合
		List<CartPromotionGoodsVo> group_list = new ArrayList<>();
		
		//遍历这个商品参与的所有活动
		for (PromotionGoodsVo vo : list) {
			
			CartPromotionGoodsVo  cartPromotionGoodsVo = new CartPromotionGoodsVo();
			cartPromotionGoodsVo.setActivity_id(vo.getActivity_id());
			cartPromotionGoodsVo.setPromotion_type(vo.getPromotion_type());
			cartPromotionGoodsVo.setStart_time(vo.getStart_time());
			cartPromotionGoodsVo.setEnd_time(vo.getEnd_time());
			cartPromotionGoodsVo.setTitle(vo.getTitle());
			
			//判断是否是组合优惠活动，目前只有满优惠活动工具是 组合活动，所以只判断满优惠活动
			if(vo.getPromotion_type().equals(PromotionTypeEnum.FULLDISCOUNT.getType())){
				group_list.add(cartPromotionGoodsVo);
				
			}else{
				
				//如果有默认参与的活动则设为 条件（activity_id不为null && activity_id 等于 当前遍历的活动id）
				if(activity_id!=null && activity_id.intValue()==vo.getActivity_id().intValue()){
					cartPromotionGoodsVo.setIs_check(1);
				}
				
				single_list.add(cartPromotionGoodsVo);
			}
		}
		
		//如果没有传递参与的单品活动 && 单品活动集合部位空
		if(activity_id==null && !single_list.isEmpty()){
			//默认设置第一个为默认参与的活动
			single_list.get(0).setIs_check(1);
		}
		
		
		product.setSingle_list(single_list);
		product.setGroup_list(group_list);
		
		return product;
	}
	
	
	/**
	 * 根据属主id 从一个集合中查找cart
	 * @param ownerid 属主id
	 * @param itemList 购物车列表
	 * @return 购物车
	 */
	protected CartVo findCart(int ownerid, List<CartVo> itemList) {
		for (CartVo item : itemList) {
			if (item.getSeller_id() ==  ownerid) {
				return item;
			}
		}
		return null;
	}
	
	
	/**
	 * 
	 * @param product 产品信息
	 * @param num	数量
	 * @param cart	购物车
	 * @param overwriteNum	
	 */
	protected void updateProductNum(Product product ,int num, Cart cart,boolean overwriteNum){
		List<Product> productList  = cart.getProductList();
		
		// **如果已经购买过更新数量及小计
		Product orginalProduct  = this.findProduct(product.getProduct_id(), productList);
		
		//已经购买过此产品
		if(orginalProduct!=null){
			
			if(!overwriteNum){
				// 原始的购买数量
				Integer originalNum = orginalProduct.getNum();
				num = originalNum + num;
			}
			
			//读取商品的信息
			GoodsSkuVo sku = this.goodsSkuManager.getSkuFromCache(product.getProduct_id());
			
			//如果购物车中总的商品数量大于库存中可用库存时，则将购物车中的商品库存设为可用库存的最大值
			if(num>sku.getEnable_quantity()){
				num = sku.getEnable_quantity();
			}
			
			orginalProduct.setNum(num);
			//修改数量的时候需要把价格重新赋值。
			orginalProduct.setPurchase_price(product.getOriginal_price());
			//计算小计金额
			orginalProduct.setSubtotal(CurrencyUtil.mul(product.getOriginal_price(),num));
		}else{
			product.setNum(num);
			product.setSubtotal(CurrencyUtil.mul(product.getOriginal_price(), num));
			productList.add(product);
		}
		
	}
	
	/**
	 * 由一个产品列表中找到产品
	 * @param productid 产品id
	 * @param productList 产品列表
	 * @return 找到的产品
	 */
	protected Product findProduct(int productid, List<Product> productList) {

		for (Product item : productList) {
			if (item.getProduct_id() == productid) {
				return item;
			}
		}
		return null;
	}
	
	
	/**
	 * 拷贝参数to活动列表
	 * @param cart
	 * @return
	 */
	protected CartVo copyProductToPromotion(Cart cart){
		
		CartVo cartVo = new CartVo();
		cartVo.setCouponList(cart.getCouponList());
		cartVo.setGiftList(cart.getGiftList());
		cartVo.setPrice(cart.getPrice());
		cartVo.setProductList(cart.getProductList());
		cartVo.setSeller_id(cart.getSeller_id());
		cartVo.setSeller_name(cart.getSeller_name());
		cartVo.setShipping_type_id(cart.getShipping_type_id());
		cartVo.setShipping_type_name(cart.getShipping_type_name());
		cartVo.setWeight(cart.getWeight());
		cartVo.setGiftCouponList(cart.getGiftCouponList());
		cartVo.setGiftPoint(cart.getGiftPoint());
		
		/**
		 * 购物车展示数据根据促销活动组合
		 */
		List<GroupPromotionVo> promotionList = new ArrayList<>();
		
		List<Integer> productIds =new LinkedList<>();
		//遍历所有商品的集合，查找组合活动的商品，并组织好活动与商品的(一对多)关系
		for(Product product:cartVo.getProductList()){
			if(product.getIs_check()==0){
				continue;
			}
			//商品已参与的组合促销集合
			List<CartPromotionGoodsVo> groupList = product.getGroup_list();
			if(!groupList.isEmpty()){
				
				CartPromotionGoodsVo cartPromotionGoodsVo = groupList.get(0);
				//活动id
				Integer activity_id = cartPromotionGoodsVo.getActivity_id();
				//活动工具类型
				String promotion_type = cartPromotionGoodsVo.getPromotion_type();
				
				//如果展示促销活动集合为空，则新建一个
				if(promotionList.isEmpty()){
					GroupPromotionVo groupPromotionVo = new GroupPromotionVo();
					List<Product> productList = new ArrayList<>();
					productList.add(product);
					groupPromotionVo.setProductList(productList);
					groupPromotionVo.setPromotion_type(promotion_type);
					String key = PromotionServiceConstant.getFullDiscountKey(activity_id);
					FullDiscountVo discountVo = (FullDiscountVo) cache.get(key);
					if(discountVo == null) {
						discountVo= fullDiscountManager.getFromDB(activity_id);
					}
					groupPromotionVo.setActivity_detail(discountVo);
					groupPromotionVo.setIs_group(1);
					promotionList.add(groupPromotionVo);
				}else{
					
					//用于判断当前商品参与的组合活动是否存在 展示促销活动集合中。
					boolean flag = false;
					//遍历展示促销活动集合
					for (GroupPromotionVo groupPromotionVo : promotionList) {
						
						//满优惠的Vo
						FullDiscountVo discountVo = (FullDiscountVo) groupPromotionVo.getActivity_detail();
						
						//判断当前商品参与的活动，是否存在于促销活动集合中。
						if(activity_id.equals(discountVo.getFd_id()) && promotion_type.equals(PromotionTypeEnum.FULLDISCOUNT.getType())){
							groupPromotionVo.getProductList().add(product);
							flag=false;
							break;	//结束循环遍历展示促销活动集合
						}else{
							flag = true;
						}
					}
					
					//如果不存在
					if(flag){
						GroupPromotionVo groupPromotionVo = new GroupPromotionVo();
						List<Product> productList = new ArrayList<>();
						productList.add(product);
						groupPromotionVo.setProductList(productList);
						groupPromotionVo.setPromotion_type(promotion_type);
						String key = PromotionServiceConstant.getFullDiscountKey(activity_id);
						FullDiscountVo discountVo = (FullDiscountVo) cache.get(key);
						groupPromotionVo.setActivity_detail(discountVo);
						groupPromotionVo.setIs_group(1);
						promotionList.add(groupPromotionVo);
					}
					
				}
				productIds.add(product.getProduct_id());
				continue;
			}
		}
		
		
		//没有参与组合活动的商品
		List<Product> singleProduct = new ArrayList<>();
		
		//遍历所有商品的集合
		for(Product product:cartVo.getProductList()){
			//如果不属于组合活动的商品，则进入
			if(!productIds.contains(product.getProduct_id())){
				singleProduct.add(product);
			}
		}
		
		if(!singleProduct.isEmpty()){
			GroupPromotionVo groupPromotionVo = new GroupPromotionVo();
			groupPromotionVo.setProductList(singleProduct);
			groupPromotionVo.setIs_group(2);
			promotionList.add(groupPromotionVo);
		}
		cartVo.setPromotionList(promotionList);
		this.countPromotionPrice(cartVo);
		
		return cartVo;
	}

	/**
	 * 重新渲染购物车列表，以便重新整理促销信息
	 * @param cartList
	 * @return
	 */
	protected List<CartVo> reRender(List<CartVo> cartList){

		//准备新的cartList，用来存储最终要形成的list
		List<CartVo> newCartList = new ArrayList<>();

		//重新形成cartList ，加入促销信息
		for (CartVo cart:cartList ) {
			if(!cart.getProductList().isEmpty()){
				//组合展示购物车数据
				CartVo cartVo = this.copyProductToPromotion(cart);
				newCartList.add(cartVo);
			}
		}

		return newCartList;


	}
	
	
	/**
	 * 计算购物车内各满优惠活动中的商品小计总和<br>
	 * 1、此处价格仅为展示购物车页时，计算满优惠活动的一些价格展示作用。
	 * @param list
	 */
	protected void countPromotionPrice(CartVo cartVo){
		
		
		List<GroupPromotionVo> promotionList = cartVo.getPromotionList();
		for (GroupPromotionVo groupPromotionVo : promotionList) {
			
			if(cartVo.getPrice().getTotal_price()<=0){
				groupPromotionVo.setSpreadPrice(0d);
				groupPromotionVo.setSubtotal(0d);
				groupPromotionVo.setDiscountPrice(0d);
				return;
			}
			
			//判断是组合活动 && 活动中是否有商品
			if(groupPromotionVo.getIs_group()!=null && groupPromotionVo.getIs_group().intValue()==1 && !groupPromotionVo.getProductList().isEmpty()){
				
				FullDiscountVo discountVo = (FullDiscountVo) groupPromotionVo.getActivity_detail();
				
				for (Product product : groupPromotionVo.getProductList()) {
					groupPromotionVo.setSubtotal(CurrencyUtil.add(groupPromotionVo.getSubtotal(), product.getSubtotal()));
				}
				
				//满金额
				Double fullMoney = discountVo.getFull_money();
				
				//活动中所有商品的总价
				Double totalSubtotal = groupPromotionVo.getSubtotal();
				
				//差额
				double spreadPrice = CurrencyUtil.sub(fullMoney, totalSubtotal);
				
				groupPromotionVo.setSpreadPrice(spreadPrice<0?0:spreadPrice);
				double discountPrice = 0.0;
				if(spreadPrice<0){
					/**满减优惠计算*/
					if(discountVo.getIs_full_minus()!=null && discountVo.getIs_full_minus()==1 && discountVo.getMinus_value() != null) {
						discountPrice = discountVo.getMinus_value();
					}
					/**满折优惠计算*/
					if(discountVo.getIs_discount()!=null && discountVo.getIs_discount()==1 && discountVo.getDiscount_value() != null) {
						double percentage = CurrencyUtil.mul(discountVo.getDiscount_value(), 0.1);
						discountPrice = CurrencyUtil.sub(totalSubtotal, CurrencyUtil.mul(totalSubtotal, percentage));
					}
					groupPromotionVo.setDiscountPrice(discountPrice);
				}
				
				//计算此活动中的每个商品的总价 所占 参与此活动所有商品总价的 百分比
				List<Product> skuList =  groupPromotionVo.getProductList();
				for (Product product : skuList) {
					//防止被除数为0的情况
					if(totalSubtotal.intValue()<=0){
						totalSubtotal=1d;
					}
					//此商品占参与此活动所有商品总价的比例
					Double ratio = CurrencyUtil.div(product.getSubtotal(),totalSubtotal);
					
					//活动优惠的金额按照上面计算出来的百分比，分配到每个商品，并计算得出单价
					
					//当前活动优惠的金额 分配到此商品时，应优惠的金额
					Double dis_Price = CurrencyUtil.mul(discountPrice, ratio);
					
					//(总价-应优惠的金额)/购买的数量 = 商品购买时的单价
					product.setPurchase_price(CurrencyUtil.div(CurrencyUtil.sub(product.getSubtotal(),dis_Price), product.getNum()));
					
					//计算商品的总价
					product.setSubtotal(CurrencyUtil.mul(product.getPurchase_price(), product.getNum()));
				}
				
			}
		}
		
	}
	
	/**
	 * 设置购物车优惠券的参数
	 * @return
	 */
	protected Coupon setCouponParam(MemberBonus bonus,Integer shop_id){
		Coupon coupon = new Coupon();
		coupon.setAmount(bonus.getType_money());
		coupon.setCoupon_id(bonus.getBonus_id());
		coupon.setEnd_time(DateUtil.toString( new Date(bonus.getUse_end_date()),"yyyy-MM-dd HH:mm:ss"));
		coupon.setSeller_id(shop_id);
		coupon.setUse_term("满"+bonus.getMin_goods_amount()+"元可用");
		return coupon;
	}
	
	/**
	 * 读取当前会员的session key
	 * @return
	 */
	protected String getSessionKey() {
		String cacheKey = OrderServiceConstant.CART_MEMBER_ID_PREFIX+ThreadContextHolder.getSession().getId();
		//如果会员登陆了，则要以会员id为key
		Member member = UserConext.getCurrentMember();
		if (member != null) {
			cacheKey = OrderServiceConstant.CART_MEMBER_ID_PREFIX+member.getMember_id();
		}
		return cacheKey;
	}
	
	
}