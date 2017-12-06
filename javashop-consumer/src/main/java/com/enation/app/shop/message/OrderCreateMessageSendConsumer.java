package com.enation.app.shop.message;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.eop.sdk.context.EopSetting;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.model.enums.MessageCodeEnum;
import com.enation.app.shop.message.model.enums.MessageStatusEnum;
import com.enation.app.shop.message.model.enums.ShopNoticeType;
import com.enation.app.shop.message.model.vo.MobileVo;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.service.IMobileSmsSendManager;
import com.enation.app.shop.message.service.IStoreMessageManager;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.util.DateUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 订单创建发送消息
 * 订单创建发送消息包含：邮件、短信、站内信
 * @author kanon
 * @version v1.0
 * @since v6.4.0 2017年9月8日
 */
@Component
public class OrderCreateMessageSendConsumer implements IOrderStatusChangeEvent{

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IMessageManager messageManager;

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private IShopManager shopManager;

	@Autowired
	private IMobileSmsSendManager mobileSmsSendManager;

	@Autowired
	private IEmailSendlManager emailSendlManager;

	@Autowired
	private IStoreMessageManager storeMessageManager;


	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if(orderMessage.getNewStatus().name().equals(OrderStatus.CONFIRM.name())) {
			try {
				if (orderMessage.getOrder() != null ) {
					storeMessageSend(orderMessage.getOrder());
				}
			} catch (Exception e) {
				this.logger.error("发送信息出错", e);
			}
		}
	}
	
	/**
	 * 发送店铺消息 获取会员订单取消订单消息模板 判断是否开启站内信、短信、邮件 发送站内信、短信、邮件内容
	 *
	 * @param order
	 *            店铺订单
	 * @param order
	 */
	private void storeMessageSend(OrderPo order) {
		// 记录店铺订单取消信息（商家中心查看）
		MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.STOREORDERSNEW.name());
		if(messageTemplate != null) {
			// 判断站内信是否开启
			if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
				storeMessageManager.addStoreContent(this.replaceContent(messageTemplate.getContent(), order),
						order.getSeller_id(),ShopNoticeType.ORDER);
			}

			// 判断短信是否开启
			if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendStoreSms(this.replaceMobileContent(messageTemplate.getSms_content(), order), order);
			}

			// 判断邮件是否开启
			if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendStoreEmail(replaceEmailContent(messageTemplate.getEmail_content(), order), order,
						messageTemplate.getEmail_title());
			}
		}
	}

	/**
	 * 替换站内信中的订单内容
	 *
	 * @param content
	 *            站内信
	 * @param order
	 *            店铺订单
	 */
	private String replaceContent(String content, OrderPo order) {
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("ordersSn", order.getSn());
		valuesMap.put("createTime", DateUtil.toString(order.getCreate_time(), "yyyy-MM-dd"));
		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 替换短信中的订单内容
	 * @param content 短信
	 * @param order 店铺订单
	 * @return
	 */
	private String replaceMobileContent(String content, OrderPo order) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("ordersSn", order.getSn());
		valuesMap.put("createTime", DateUtil.toString(order.getCreate_time(), "yyyy-MM-dd"));
		valuesMap.put("siteName", EopSite.getInstance().getSitename());
		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 替换邮件内容
	 *
	 * @param emailContent
	 *            邮件信息内容
	 * @param order
	 *            店铺订单
	 * @return 邮件内容
	 */
	private String replaceEmailContent(String emailContent, OrderPo order) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Configuration cfg = new Configuration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("emailContent", emailContent);
		cfg.setTemplateLoader(stringLoader);
		try {
			Template template = cfg.getTemplate("emailContent", "utf-8");
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("ordersSn", order.getSn());
			root.put("createTime", DateUtil.toString(order.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
			root.put("siteName", EopSite.getInstance().getSitename());
			StringWriter writer = new StringWriter();
			template.process(root, writer);
			return writer.toString();
		} catch (Exception e) {
			this.logger.error("发送信息-替换邮件内容出错", e);
			return null;
		}
	}

	/**
	 * 发送店铺短信 此处店铺收件人为联系电话，此处可后期升级更改
	 * 
	 * @param smsContent
	 *            短信内容
	 * @param order
	 *            店铺订单
	 */
	private void sendStoreSms(String smsContent, OrderPo order) {
		ShopDetail shopDetail = shopManager.getShopDetail(order.getSeller_id());

		// 发送短信
		MobileVo mobileVo = new MobileVo();
		mobileVo.setContent(smsContent);
		mobileVo.setMobile(shopDetail.getLink_phone());
		/** 调发送短信的接口 */
		mobileSmsSendManager.sendMqMsg(mobileVo);
		// 记录日志
		storeMessageManager.addStoreSmsContent(smsContent, shopDetail.getShop_id());
	}

	/**
	 * 发送邮件
	 *
	 * @param emailContent
	 *            邮件内容
	 * @param order
	 *            店铺订单 email 店铺邮箱
	 * @param emailTitle
	 *            邮件标题
	 */
	private void sendStoreEmail(String emailContent,OrderPo order, String emailTitle) {

		ShopDetail shopDetail = shopManager.getShopDetail(order.getSeller_id());

		EmailModel emailModel = new EmailModel();
		emailModel.setTitle(emailTitle);
		emailModel.setEmail(shopDetail.getCompant_email());
		emailModel.setContent(emailContent);
		emailSendlManager.sendMqMsg(emailModel);

	}

	

}
