package com.enation.app.shop.payment.service;

import java.util.List;

import com.enation.app.shop.payment.model.enums.ClientType;
import com.enation.app.shop.payment.model.po.PaymentMethod;
import com.enation.app.shop.payment.model.vo.PaymentMethodVo;
import com.enation.app.shop.payment.model.vo.PaymentPluginVo;


/**
 * 支付方式业务管理接口
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月27日下午3:45:20	
 */
public interface IPaymentMethodManager {
	
	/**
	 * 获取某个client可以全名用的支付方式表
	 * @param clientType 客户端类型
	 * @return 支付方式列表
	 */
	public List<PaymentMethodVo> list(ClientType clientType);
	
	
	/**
	 * 获取App的支付式列表<br>
	 * 包含所有配置
	 * <br>
	 * 是加密的，密钥在系统设置的
	 * @return
	 */
	public String listApp();

	/**
	 * 根据支付插件id获取支付方式详细
	 * @param payment_plugin_id
	 * @return
	 */
	public PaymentMethod getByPluginId(String payment_plugin_id);
	
	/**
	 * 后台获取支付插件列表
	 * @return
	 */
	public List<PaymentPluginVo> getAllPlugins();

	/**
	 * 根据插件id返回支付方式vo
	 * @param plugin_id
	 * @return
	 */
	public PaymentPluginVo getByPlugin(String plugin_id);

	/**
	 * 保存
	 * @param vo
	 * @return
	 */
	public PaymentPluginVo save(PaymentPluginVo vo);
	
	
}
