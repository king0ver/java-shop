package com.enation.app.shop.message;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IGoodsChangeEvent;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
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
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.support.goods.GoodsChangeMsg;
import com.enation.framework.util.StringUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 
 * 管理员下架商品时发送站内消息
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月20日 上午11:36:24
 */
@Component
public class StoreGoodsMarketEnableConsumer implements IGoodsChangeEvent {
	protected final Logger logger = Logger.getLogger(getClass());
	@Autowired
	private IStoreMessageManager storeMessageManager;
	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private IMessageManager messageManager;
	@Autowired
	private IMobileSmsSendManager mobileSmsSendManager;
	@Autowired
	private IShopManager storeManager;
	@Autowired
	private IEmailSendlManager emailSendlManager;
	
	@Override
	public void goodsChange(GoodsChangeMsg goodsChangeMsg) {
		int operation_type = goodsChangeMsg.getOperation_type();
		if (GoodsChangeMsg.UNDER_OPERATION == operation_type&&!StringUtil.isEmpty(goodsChangeMsg.getMessage())) {
			for (Integer goods_id : goodsChangeMsg.getGoods_ids()) {
				storeMessageSend(goodsManager.getFromCache(goods_id), goodsChangeMsg.getMessage());
			}
		}
	}


	/**
	 * 发送店铺消息 获取会员订单取消订单消息模板 判断是否开启站内信、短信、邮件 发送站内信、短信、邮件内容
	 *
	 * @param goodsVo
	 *            店铺商品
	 * @param message
	 *            审核消息
	 */
	private void storeMessageSend(GoodsVo goodsVo, String reason) {
		// 记录店铺订单取消信息（商家中心查看）
		MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.STOREGOODSMARKETENABLE.name());
		if(messageTemplate != null) {
			// 判断站内信是否开启
			if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
				storeMessageManager.addStoreContent(this.replaceContent(messageTemplate.getContent(), goodsVo, reason),
						goodsVo.getSeller_id(), ShopNoticeType.GOODS);
			}

			// 判断短信是否开启
			if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendStoreSms(this.replaceMobileContent(messageTemplate.getSms_content(), goodsVo, reason), goodsVo);
			}

			// 判断邮件是否开启
			if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
				sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), goodsVo, reason), goodsVo,
						messageTemplate.getEmail_title());
			}
		}

	}

	/**
	 * 发送店铺短信 发送店铺短信并记录短息发送日志 此处店铺收件人为联系电话，此处可后期升级更改
	 * 
	 * @param smsContent
	 *            短信内容
	 * @param goodsVo
	 *            店铺商品
	 */
	private void sendStoreSms(String smsContent, GoodsVo goodsVo) {

		ShopDetail shopDetail = storeManager.getShopDetail(goodsVo.getSeller_id());

		MobileVo mobileVo = new MobileVo();
		mobileVo.setContent(smsContent);
		mobileVo.setMobile(shopDetail.getLink_phone());
		/** 调发送短信的接口 */
		mobileSmsSendManager.sendMqMsg(mobileVo);
		storeMessageManager.addStoreSmsContent(smsContent, shopDetail.getShop_id());
	}

	/**
	 * 替换站内信中的订单内容
	 *
	 * @param content
	 *            站内信
	 * @param goodsVo
	 *            店铺商品
	 * @param message
	 *            审核信息
	 */
	private String replaceContent(String content, GoodsVo goodsVo, String reason) {

		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("name", goodsVo.getGoods_name());
		valuesMap.put("reason", reason);
		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 发送邮件
	 *
	 * @param emailContent
	 *            邮件内容
	 * @param goodsVo
	 *            店铺商品
	 * @param emailTitle
	 *            邮件标题
	 */
	private void sendEmail(String emailContent, GoodsVo goodsVo, String emailTitle) {

		ShopDetail shopDetail = storeManager.getShopDetail(goodsVo.getSeller_id());

		EmailModel emailModel = new EmailModel();
		emailModel.setTitle(emailTitle);
		emailModel.setEmail(shopDetail.getCompant_email());
		emailModel.setContent(emailContent);
		emailSendlManager.sendMqMsg(emailModel);

	}

	/**
	 * 替换短信中的订单内容
	 * 
	 * @param content
	 *            短信内容
	 * @param goodsVo
	 *            店铺商品
	 * @param message
	 *            审核信息
	 * @return
	 */
	public String replaceMobileContent(String content, GoodsVo goodsVo, String reason) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Map<String, Object> valuesMap = new HashMap<String, Object>();
		valuesMap.put("siteName", EopSite.getInstance().getSitename());
		valuesMap.put("name", goodsVo.getGoods_name());
		valuesMap.put("reason", reason);
		StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
		return strSubstitutor.replace(content);
	}

	/**
	 * 替换邮件内容
	 *
	 * @param emailContent
	 *            邮件信息内容
	 * @param goodsVo
	 *            店铺商品
	 * @return 邮件内容
	 */
	private String replaceEmailContent(String emailContent, GoodsVo goodsVo, String reason) {
		/** 已经安装 */
		EopSetting.INSTALL_LOCK = "YES";
		Configuration cfg = new Configuration();
		StringTemplateLoader stringLoader = new StringTemplateLoader();
		stringLoader.putTemplate("emailContent", emailContent);
		cfg.setTemplateLoader(stringLoader);
		try {
			Template template = cfg.getTemplate("emailContent", "utf-8");
			Map<String, Object> root = new HashMap<String, Object>();
			root.put("name", goodsVo.getGoods_name());
			root.put("siteName", EopSite.getInstance().getSitename());
			root.put("reason", reason);
			StringWriter writer = new StringWriter();
			template.process(root, writer);
			return writer.toString();
		} catch (Exception e) {
			this.logger.error("发送信息-替换邮件内容出错", e);
			return null;
		}
	}

}
