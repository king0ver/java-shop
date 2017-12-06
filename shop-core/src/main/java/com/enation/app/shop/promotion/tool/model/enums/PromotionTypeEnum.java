package com.enation.app.shop.promotion.tool.model.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * 促销活动工具枚举
 * <p>类中注释的Cart 指com.enation.app.shop.trade.model.vo.Cart
 * @author xulipeng
 * @since V6.4
 * @version v1.0
 * 2017年08月18日17:55:35
 */
public enum PromotionTypeEnum {
	
	/**
	 * 不参与活动（指不参与任何单品活动）
	 */
	NO("no","no","不参与活动"),
	
	/**
	 * 
	 * 计算价格时: 
	 * 1、Cart.productList.Product.purchase_price 需要修改。<br>
	 * 2、Cart.price.discount_price 需要累加。<br>
	 * 3、Cart.productList.Product.subtotal 需要计算<br>
	 */
	MINUS("minus","minusPlugin","单品立减活动"),		
	
	/**
	 * 
	 * 计算价格时: 
	 * 1、Cart.productList.Product.purchase_price 需要修改。<br>
	 * 2、Cart.price.discount_price 需要累加。<br>
	 * 3、Cart.productList.Product.subtotal 需要计算<br>
	 */
	GROUPBUY("groupbuy","groupBuyGoodsPlugin","团购活动"),
	
	/**
	 * 
	 * 计算价格时: 
	 * 1、Cart.productList.Product.point 需要修改。<br>
	 * 2、Cart.price.exchange_point 需要累加。<br>
	 * 3、Cart.price.discount_price 需要累加。<br>
	 * 4、Cart.productList.Product.subtotal 需要计算<br>
	 */
	EXCHANGE("exchange","exchangePlugin","积分换购活动"),
	
	/**
	 * 
	 * 计算价格时: 
	 * 1、Cart.price.discount_price 需要累加。<br>
	 * 2、Cart.productList.Product.subtotal 需要计算<br>
	 */
	HALFPRICE("half_price","halfPricePlugin","第二件半价活动"),
	
	/**
	 * 
	 * 计算价格时: 
	 * 1、Cart.price.discount_price 需要累加。<br>
	 */
	FULLDISCOUNT("full_discount","fullDiscountPlugin","满优惠活动");
	
	private String type;
	private String pluginId;
	private String name;

	// 构造方法
	private PromotionTypeEnum(String type, String pluginId,String name) {
		this.type = type;
		this.pluginId = pluginId;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPluginId() {
		return pluginId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	//--------------------------------------------------
	//--------------------------------------------------
	
	
	/**
	 * 读取单品活动集合
	 * @return
	 */
	public static List<String> getSingle(){
		List<String> pluginId = new ArrayList<>();
		pluginId.add(PromotionTypeEnum.EXCHANGE.getPluginId());
		pluginId.add(PromotionTypeEnum.GROUPBUY.getPluginId());
		pluginId.add(PromotionTypeEnum.HALFPRICE.getPluginId());
		pluginId.add(PromotionTypeEnum.MINUS.getPluginId());
		return pluginId;
	}
	
	/**
	 * 读取组合活动集合
	 * @return
	 */
	public static List<String> getGroup(){
		List<String> pluginId = new ArrayList<>();
		pluginId.add(PromotionTypeEnum.FULLDISCOUNT.getPluginId());
		return pluginId;
	}
	
	/**
	 * 判断是否是单品活动
	 * @param type
	 * @return
	 */
	public static boolean isSingle(String type){
		if (PromotionTypeEnum.EXCHANGE.type.equals(type)) {
			return true;
		} else if (PromotionTypeEnum.GROUPBUY.type.equals(type)) {
			return true;
		} else if (PromotionTypeEnum.HALFPRICE.type.equals(type)) {
			return true;
		} else if (PromotionTypeEnum.MINUS.type.equals(type)) {
			return true;
		} 
		return false;
	}
	
	/**
	 * 判断是否是组合活动
	 * @param type
	 * @return
	 */
	public boolean isGroup(String type){
		if (PromotionTypeEnum.FULLDISCOUNT.type.equals(type)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取插件名称。根据type字段
	 * @param type
	 * @return 
	 */
	public static String getPlugin(String type) {
		if (PromotionTypeEnum.EXCHANGE.type.equals(type)) {
			return PromotionTypeEnum.EXCHANGE.pluginId;
		} else if (PromotionTypeEnum.FULLDISCOUNT.type.equals(type)) {
			return PromotionTypeEnum.FULLDISCOUNT.pluginId;
		} else if (PromotionTypeEnum.GROUPBUY.type.equals(type)) {
			return PromotionTypeEnum.GROUPBUY.pluginId;
		} else if (PromotionTypeEnum.HALFPRICE.type.equals(type)) {
			return PromotionTypeEnum.HALFPRICE.pluginId;
		} else if (PromotionTypeEnum.MINUS.type.equals(type)) {
			return PromotionTypeEnum.MINUS.pluginId;
		} else {
			return "";
		}
	}

	
}
