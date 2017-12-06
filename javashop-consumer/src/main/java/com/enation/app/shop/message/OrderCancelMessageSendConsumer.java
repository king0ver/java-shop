package com.enation.app.shop.message;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.enation.eop.sdk.context.EopSetting;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import com.enation.app.base.core.model.Member;
import com.enation.app.core.event.IOrderStatusChangeEvent;
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
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.util.DateUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * 订单取消发送消息
 * 订单取消发送消息包含：邮件、短信、站内信
 * @author kanon
 * @version v1.0
 * @since v6.4.0 2017年9月8日
 */
@Component
public class OrderCancelMessageSendConsumer implements IOrderStatusChangeEvent{

	private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IMessageManager messageManager;
    
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
    private IStoreMessageManager storeMessageManager;
    
    @Autowired
    private IEmailSendlManager emailSendlManager;

    public static boolean IS_DEMO_SITE=false; //是否是演示站

	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if(orderMessage.getNewStatus().name().equals(OrderStatus.CANCELLED.name())) {
			// 发送会员消息
	        memberMessageSend(orderMessage.getOrder());

	        // 发送店铺消息
	        storeMessageSend(orderMessage.getOrder());
		}	
	}
	
	 /**
     * 发送会员消息 获取会员取消订单消息模板 判断是否开启站内信、短信、邮件 发送站内信、短信、邮件内容
     *
     * @param orderPo 店铺订单
     */
    private void memberMessageSend(OrderPo orderPo) {
        // 记录会员订单取消信息（会员中心查看）
        MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.MEMBERORDERSCANCEL.name());
        if(messageTemplate != null) {
        	 // 判断站内信是否开启
            if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
            	memberMessageManager.addMemberContent(this.replaceContent(messageTemplate.getContent(), orderPo),
            			orderPo.getMember_id());
            }

            // 判断短信是否开启
            if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
                sendMemberSms(this.replaceMobileContent(messageTemplate.getSms_content(), orderPo), orderPo);
            }

            // 判断邮件是否开启
            if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
                sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), orderPo), orderPo,
                        messageTemplate.getEmail_title());
            }
        }
    }

    /**
     * 发送店铺消息 获取会员订单取消订单消息模板 判断是否开启站内信、短信、邮件 发送站内信、短信、邮件内容
     *
     * @param orderPo 店铺订单
     */ 
    private void storeMessageSend(OrderPo orderPo) {
        // 记录店铺订单取消信息（商家中心查看）
        MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.STOREORDERSCANCEL.toString());
        if(messageTemplate != null) {
        	// 判断站内信是否开启
            if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
            		storeMessageManager.addStoreContent(this.replaceContent(messageTemplate.getContent(), orderPo),
            				orderPo.getSeller_id(),ShopNoticeType.ORDER);
            }

            // 判断短信是否开启
            if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
                sendStoreSms(this.replaceMobileContent(messageTemplate.getSms_content(), orderPo), orderPo);
            }

            // 判断邮件是否开启
            if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
                sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), orderPo), orderPo,
                        messageTemplate.getEmail_title());
            }
        }
    }

    /**
     * 替换站内信中的订单内容
     *
     * @param content 站内信
     * @param orderPo   店铺订单
     */
    private String replaceContent(String content, OrderPo orderPo) {
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("ordersSn", orderPo.getSn());
        valuesMap.put("cancelTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));

        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        return strSubstitutor.replace(content);
    }

    /**
     * 替换短信中的订单内容
     * @param content 短信
     * @param orderPo 店铺订单
     * @return
     */
    private String replaceMobileContent(String content, OrderPo orderPo) {
        /** 已经安装 */
        EopSetting.INSTALL_LOCK = "YES";
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("ordersSn", orderPo.getSn());
        valuesMap.put("cancelTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
        valuesMap.put("siteName", EopSite.getInstance().getSitename());
        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        return strSubstitutor.replace(content);
    }

    /**
     * 发送会员短信
     *
     * @param smsContent 短信内容
     * @param orderPo 店铺订单
     */
    private void sendMemberSms(String smsContent, OrderPo orderPo) {
        Member member = memberManager.getMemberByMemberId(orderPo.getMember_id());
        MobileVo mobileVo = new MobileVo();
		mobileVo.setContent(smsContent);
		mobileVo.setMobile(member.getMobile());
		mobileSmsSendManager.sendMqMsg(mobileVo);
        // 记录日志
        memberMessageManager.addMemberSmsContent(smsContent, member.getMember_id());
    }

    /**
     * 发送店铺短信
     * 此处店铺收件人为联系电话，此处可后期升级更改
     * @param smsContent 短信内容
     * @param orderPo 店铺订单
     */
    private void sendStoreSms(String smsContent, OrderPo orderPo) {
    	 	ShopDetail storeDetail = shopManager.getShopDetail(orderPo.getSeller_id());
    	 	 MobileVo mobileVo = new MobileVo();
    	 	 mobileVo.setContent(smsContent);
    	 	 mobileVo.setMobile(storeDetail.getLink_phone());
    	 	 mobileSmsSendManager.sendMqMsg(mobileVo);

        // 记录日志
        storeMessageManager.addStoreSmsContent(smsContent, storeDetail.getShop_id());
    }

    /**
     * 替换邮件内容
     *
     * @param emailContent 邮件信息内容
     * @param orderPo   店铺订单
     * @return 邮件内容
     */
    private String replaceEmailContent(String emailContent, OrderPo orderPo) {
        /** 已经安装 */
        EopSetting.INSTALL_LOCK = "YES";
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("emailContent", emailContent);
        cfg.setTemplateLoader(stringLoader);
        try {
            Template template = cfg.getTemplate("emailContent", "utf-8");
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("orderSn", orderPo.getSn());
            root.put("createTime", DateUtil.toString(orderPo.getCreate_time(), "yyyy-MM-dd HH:mm:ss"));
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
     * @param emailContent 邮件内容
     * @param orderPo   店铺订单 compant_email 店铺邮箱
     * @param emailTitle   邮件标题
     */
    private void sendEmail(String emailContent, OrderPo orderPo, String emailTitle) {

        ShopDetail shopDetail = shopManager.getShopDetail(orderPo.getSeller_id());

        EmailModel emailModel = new EmailModel();
        emailModel.setTitle(emailTitle);
        emailModel.setEmail(shopDetail.getCompant_email());
        emailModel.setContent(emailContent);
        emailSendlManager.sendMqMsg(emailModel);

    }


}
