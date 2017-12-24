package com.enation.app.shop.trade.service;

/**
 *
 * 交易及订单编号产生器
 * Created by kingapex on 2017/10/16.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2017/10/16
 */
public interface ITradeSnCreator {

    /**
     * 生成交易编号
     * @return 交易编号
     */
    String generateTradeSn();


    /**
     * 生成订单编号
     * @return 订单编号
     */
    String generateOrderSn();

    /**
     * 清除
     */
    void cleanCache();

    /**
     * 生成充值訂單號
     * @return
     */
    String generateRechangeSn();

}
