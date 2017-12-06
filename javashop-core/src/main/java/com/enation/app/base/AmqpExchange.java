package com.enation.app.base;

/**
 * AMQP消息定义
 * @author kingapex
 * @version 1.0
 * @since 6.4
 * 2017-08-17 18：00
 */
public enum AmqpExchange {

    PC_INDEX_CHANGE("PC首页变化消息"),
    MOBILE_INDEX_CHANGE("移动端首页变化消息"),
    GOODS_CHANGE("商品变化消息"),
    GOODS_CHANGE_REASON("商品变化消息附带原因"),
    HELP_CHANGE("帮助变化消息"),
    PAGE_CREATE("页面生成消息"),
    INDEX_CREATE("索引生成消息"),
    ORDER_CREATE("订单创建消息"), //没有入库
    ORDER_INTODB_ERROR("入库失败消息"),//入库失败
    ORDER_STATUS_CHANGE("订单状态变化消息"), //带入库的
    MEMEBER_LOGIN("会员登录消息"),
    MEMEBER_REGISTER("会员注册消息"),
    SHOP_CHANGE_REGISTER("店铺变更消息"),
    GOODS_CATEGORY_CHANGE("分类变更消息"),
    REFUND_STATUS_CHANGE("售后状态改变消息"),
    MEMBER_MESSAGE("发送站内信息"), 
    SMS_SEND_MESSAGE("发送手机短信消息"),
    EMAIL_SEND_MESSAGE("邮件发送消息"),
    GOODS_COMMENT_COMPLETE("商品评论消息"),
    ONLINE_PAY("网上支付"),
    MEMBER_INFO_COMPLETE("完善个人资料")
    ;

    private String description;
    AmqpExchange(String _description){
		  this.description=_description;
    }

    public String getDescription(){
    	return description;
    }
    

}
