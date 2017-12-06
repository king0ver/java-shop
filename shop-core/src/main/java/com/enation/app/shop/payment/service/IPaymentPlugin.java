package com.enation.app.shop.payment.service;

import java.util.List;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.aftersale.model.vo.RefundBill;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.payment.model.vo.PayBill;
import com.enation.app.shop.trade.model.enums.TradeType;

/**
 * 在线支付事件
 * 
 * @author kingapex
 *
 */
public interface IPaymentPlugin {

	/**
	 * 生成跳转至第三方支付平台的html和脚本
	 * 
	 * @param order
	 *            可支付的对象
	 * @return 跳转到第三方支付平台的html和脚本
	 */
	public String onPay(PayBill bill);

	/**
	 * 同步回调
	 * 
	 * @throws 如果失败抛出RuntimeException异常
	 */
	public String onReturn(TradeType tradeType);

	/**
	 * 异步回调
	 * 
	 * @return 原样返回插件的返回值。
	 */
	public String onCallback(TradeType tradeType);
	
	
	/**
	 * 退款，原路退回
	 * @param bill 
	 */
	public boolean returnPay(RefundBill bill);
	
	/**
	 * 查询退款的结果
	 */
	public RefundPartVo queryRefundStatus(RefundPartVo refund);

	/**
	 * 获取支付插件id
	 * @return
	 */
	public String getPluginId();
	
	/**
	 * 支付名称
	 * @return
	 */
	public String getPluginName();
	
	/**
	 * 定义
	 * 
	 * @return
	 */
	public List<ConfigItem> definitionConfigItem();

	/**
	 * 是否支持原路退回   0 不支持  1支持
	 * @return
	 */
	public Integer getIsRetrace();


}
