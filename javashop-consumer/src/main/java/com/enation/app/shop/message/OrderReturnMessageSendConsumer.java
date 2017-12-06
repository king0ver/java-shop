package com.enation.app.shop.message;

import java.util.HashMap;

import java.io.StringWriter;
import java.util.Map;

import com.enation.app.core.event.IRefundStatusChangeEvent;
import com.enation.eop.sdk.context.EopSetting;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.model.enums.MessageCodeEnum;
import com.enation.app.shop.message.model.enums.MessageStatusEnum;
import com.enation.app.shop.message.model.enums.ShopNoticeType;
import com.enation.app.shop.message.model.vo.MobileVo;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.service.IMobileSmsSendManager;
import com.enation.app.shop.message.service.IStoreMessageManager;
import com.enation.app.shop.shop.apply.model.po.ShopDetail;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.jms.EmailModel;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 订单退货发送消息 订单售后发送消息包含：邮件、短信、站内信
 * 
 * @author kanon
 * @version v1.0
 * @since v6.4.0 2017年9月8日
 */
@Component
public class OrderReturnMessageSendConsumer implements IRefundStatusChangeEvent {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IMessageManager messageManager;

	@Autowired
	private IStoreMessageManager storeMessageManager;

	@Autowired
	private IMemberMessageManager memberMessageManager;

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private IShopManager shopManager;

	@Autowired
	private IMobileSmsSendManager mobileSmsSendManager;

	@Autowired
	private IMemberManager memberManager;

	@Autowired
	private IEmailSendlManager emailSendlManager;

	@Override
	public void refund(RefundChangeMessage refundPartVo) {
		if (refundPartVo.getOperation_type().equals(RefundChangeMessage.APPLY)) {
			OrderDetail orderDetail = this.orderQueryManager.getOneBySn(refundPartVo.getRefund().getOrder_sn());
			if (orderDetail != null) {
				// 会员信息发送
				memberMessageSend(refundPartVo, orderDetail);

				// 店铺信息发送
				storeMessageSend(orderDetail, refundPartVo);

			}
		}
	}

	/**
	 * 发送会员消息 获取订单售后消息模板 判断是否开启站内信、短信、邮件 发送站内信、短信、邮件内容
	 *
	 * @param orderDetail
	 *            店铺订单
	 * @param refundPartVo
	 *            售后单
	 */
	private void memberMessageSend(RefundChangeMessage refundPartVo, OrderDetail orderDetail) {
		// 记录会员订单取消信息（会员中心查看）
		MessageTemplate messageTemplate = messageManager
				.getMessageTemplate(MessageCodeEnum.MEMBERRETURNUPDATE.toString());
		if (messageTemplate != null) {
			// 判断站内信是否开启
			if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
				memberMessageManager.addMemberContent(this.replaceContent(messageTemplate.getContent(), refundPartVo),
						orderDetail.getMember_id());
			}

			// 判断短信是否开启
			if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendMemberSms(this.replaceMobileContent(messageTemplate.getSms_content(), refundPartVo), orderDetail);
			}

			// 判断邮件是否开启
			if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), refundPartVo), orderDetail,
						messageTemplate.getEmail_title());
			}
		}
	}

	/**
	 * 发送店铺消息 获取订单售后消息模板 判断是否开启站内信、短信、邮件 发送站内信、短信、邮件内容
	 *
	 * @param orderDetail
	 *            店铺订单
	 * @param refundPartVo
	 *            售后单
	 */
	private void storeMessageSend(OrderDetail orderDetail, RefundChangeMessage refundPartVo) {
		// 记录店铺订单取消信息（商家中心查看）
		MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.STORERETURN.toString());
		if (messageTemplate != null) {
			// 判断站内信是否开启
			if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
				storeMessageManager.addStoreContent(this.replaceContent(messageTemplate.getContent(), refundPartVo),
						orderDetail.getSeller_id(), ShopNoticeType.AFTERSALE);
			}

			// 判断短信是否开启
			if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendStoreSms(this.replaceMobileContent(messageTemplate.getSms_content(), refundPartVo), orderDetail);
			}

			// 判断邮件是否开启
			if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), refundPartVo), orderDetail,
						messageTemplate.getEmail_title());
			}
		}
	}

	/**
	 * 替换站内信中的订单内容
	 *
	 * @param content
	 *            站内信
	 * @param refundPartVo
	 *            退货单
	 */
	private String replaceContent(String content, RefundChangeMessage refundPartVo) {

		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("refundSn", refundPartVo.getRefund().getSn());

		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 替换短信中的订单内容
	 * 
	 * @param content
	 *            短信
	 * @param refundPartVo
	 *            退货单
	 * @return
	 */
	private String replaceMobileContent(String content, RefundChangeMessage refundPartVo) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("refundSn", refundPartVo.getRefund().getSn());
		valuesMap.put("siteName", EopSite.getInstance().getSitename());
		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 发送会员短信
	 *
	 * @param smsContent
	 *            短信内容
	 * @param orderDetail
	 *            店铺订单
	 */
	private void sendMemberSms(String smsContent, OrderDetail orderDetail) {
		Member member = memberManager.get(orderDetail.getMember_id());
		// 发送短信
		MobileVo mobileVo = new MobileVo();
		mobileVo.setContent(smsContent);
		mobileVo.setMobile(member.getMobile());
		/** 调发送短信的接口 */
		mobileSmsSendManager.sendMqMsg(mobileVo);

		// 记录日志
		memberMessageManager.addMemberSmsContent(smsContent, member.getMember_id());
	}

	/**
	 * 发送店铺短信
	 *
	 * @param smsContent
	 *            短信内容
	 * @param orderDetail
	 *            店铺订单
	 */
	private void sendStoreSms(String smsContent, OrderDetail orderDetail) {
		ShopDetail shopDetail = shopManager.getShopDetail(orderDetail.getSeller_id());
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
	 * 替换邮件内容
	 *
	 * @param emailContent
	 *            邮件信息内容
	 * @param refundPartVo
	 *            退款单
	 * @return 邮件内容
	 */
	private String replaceEmailContent(String emailContent, RefundChangeMessage refundPartVo) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Configuration cfg = new Configuration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("emailContent", emailContent);
		cfg.setTemplateLoader(stringLoader);
		try {
			Template template = cfg.getTemplate("emailContent", "utf-8");
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("refundSn", refundPartVo.getRefund().getSn());
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
	 * 发送邮件
	 *
	 * @param emailContent
	 *            邮件内容
	 * @param orderDetail
	 *            店铺订单 email 店铺邮箱
	 * @param emailTitle
	 *            邮件标题
	 */
	private void sendEmail(String emailContent, OrderDetail orderDetail, String emailTitle) {

		ShopDetail shopDetail = shopManager.getShopDetail(orderDetail.getSeller_id());

		EmailModel emailModel = new EmailModel();
		emailModel.setTitle(emailTitle);
		emailModel.setEmail(shopDetail.getCompant_email());
		emailModel.setContent(emailContent);
		emailSendlManager.sendMqMsg(emailModel);

	}

}
