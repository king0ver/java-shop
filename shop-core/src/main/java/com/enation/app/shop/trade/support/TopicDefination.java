package com.enation.app.shop.trade.support;

public class TopicDefination {
 
	public final static String CMS_MANAGE_TOPIC = "cms-manage-service.topic";//cms数据变化消息
	
	public final static String CMS_TEMPLATE_TOPIC = "cms-template-service.topic";//cms模板变化消息
	
	public final static String ORDER_CREATE_TOPIC = "order-service.goods.topic";//订单创建消息
	
	public final static String ORDER_CHANGE_TOPIC = "order-service.topic";//订单修改消息
	
	public final static String MEMBER_REGISTER_TOPIC = "member-service.register.topic";//会员注册消息
	
	public final static String MEMEBER_LOGIN_TOPIC = "member-service.login.topic";//会员登录消息
	 
	public final static String SHOP_CHANGE_TOPIC = "shop-service.change.topic";//店铺变更消息 
	
	public static final String ORDER_STATUS_TOPIC = "order.status.topic";

	public final static String GOODS_CATEGORY_CHANGE_TOPIC = "goods-service.category.change.topic";//分类变更消息
	
	public final static String GOODS_CHANGE_TOPIC = "goods-service.goods.change.topic";//商品变更消息
	
	public final static String GOODS_BROWSE_TOPIC = "goods-service.goods.browse.topic";//商品浏览消息
	
	public final static String MEMBER_MESSAGE_TOPIC = "member-service.message.add";//发送站内信息
	
	public final static String GOODS_COMMENT_TOPIC = "comment-service.comment.add";//商品评论消息
	
	public final static String REFUND_PASS = "order-bill.refund.pass";//退款审核通过
}
