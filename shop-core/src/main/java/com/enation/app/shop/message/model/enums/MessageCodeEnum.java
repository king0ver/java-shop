package com.enation.app.shop.message.model.enums;
/**
 * 消息模板编号
 * @author Kanon
 * @since 6.4.0
 * @version 1.0
 * 2017-8-3
 */
public enum MessageCodeEnum {
    
    STOREORDERSNEW("店铺新订单创建提醒"),
    STOREORDERSPAY("店铺订单支付提醒"),
    STOREORDERSRECEIVE("店铺订单收货提醒"),
    STOREORDERSEVALUATE("店铺订单评价提醒"),
    STOREORDERSCANCEL("店铺订单取消提醒"),
    STOREREFUND("店铺退款提醒"),
    STORERETURN("店铺退货提醒"),
    STOREGOODSVIOLATION("商品违规被禁售提醒（商品下架）"),
    STOREGOODSVERIFY("商品审核失败提醒"),
    MEMBERRETURNUPDATE("退货提醒"),
    MEMBERREFUNDUPDATE("退款提醒"),
    MEMBERORDERSSEND("订单发货提醒"),
    MEMBERORDERSRECEIVE("订单收货提醒"),
    MEMBERORDERSPAY("订单支付提醒"),
    MEMBERORDERSCANCEL("订单取消提醒"),
    MOBILECODESEND("手机发送验证码"),
	STOREGOODSMARKETENABLE("商品下架消息提醒"),
    MEMBERLOGINSUCCESS("会员登陆成功提醒"),
    MEMBERREGISTESUCCESS("会员注册成功提醒");
	
    // 构造方法
    private MessageCodeEnum(String key) {
        this.key = key;
    }

    public String getKey(){
        return this.key;
    }

    private String key;
}
