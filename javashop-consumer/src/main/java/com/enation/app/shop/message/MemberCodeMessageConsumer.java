package com.enation.app.shop.message;

import com.enation.app.core.event.ISmsSendMessageEvent;
import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.model.enums.MessageCodeEnum;
import com.enation.app.shop.message.model.enums.MessageStatusEnum;
import com.enation.app.shop.message.model.vo.MobileVo;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.service.IMobileSmsSendManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.util.StringUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送验证码
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月8日
 */
@Service
public class MemberCodeMessageConsumer implements ISmsSendMessageEvent {

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IMessageManager messageManager;

    @Autowired
    private IMobileSmsSendManager mobileSmsSendManager;

    @Autowired
    private IEmailSendlManager emailSendlManager;

    @Override
    public void sendMessage(Map<String, Object> data) {
        //发送会员短息
        memberMessageSend(data);
    }

    private void memberMessageSend(Map<String, Object> data){
        String byName = StringUtil.toString(data.get("byName"));
        String code = StringUtil.toString(data.get("code"));
        String mobile = StringUtil.toString(data.get("mobile"));
        String email = StringUtil.toString(data.get("email"));
        String email_title = StringUtil.toString(data.get("emailTitle"));
        // 记录会员登陆成功信息（会员中心查看）
        MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.MOBILECODESEND.name());
     	if(messageTemplate != null) {
     	// 判断短信是否开启
            if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
                /* 判断有没有手机，有手机则发送短信 */
                if(!StringUtil.isEmpty(mobile)){
                    sendMemberSms(this.replaceMobileContent(messageTemplate.getSms_content(), byName, code), mobile);
                }
            }
            // 判断邮件是否开启
            if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
                /* 判断有没有邮箱，有邮箱则发送邮件 */
                if(!StringUtil.isEmpty(email)){
                    sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), byName, code), mobile,
                            email_title);
                }
            }
     	}

    }


    /**
     * 替换短信中的订单内容
     * @param content 短信
     * @param byName 发送短信时所做的操作
     * @param code 验证码
     * @return
     */
    private String replaceMobileContent(String content, String byName, String code) {
        /** 已经安装 */
        EopSetting.INSTALL_LOCK = "YES";
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("byName", byName);
        valuesMap.put("code", code);
        valuesMap.put("siteName", EopSite.getInstance().getSitename());
        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        return strSubstitutor.replace(content);
    }

    /**
     * 发送会员短信
     *
     * @param smsContent 短信内容
     * @param mobile 发送的手机
     */
    private void sendMemberSms(String smsContent, String mobile) {
        MobileVo mobileVo = new MobileVo();
        mobileVo.setContent(smsContent);
        mobileVo.setMobile(mobile);
        mobileSmsSendManager.sendMqMsg(mobileVo);
    }


    /**
     * 替换邮件内容
     *
     * @param emailContent 邮件信息内容
     * @param byName 发送短信时所做的操作
     * @param code 验证码
     * @return 邮件内容
     */
    private String replaceEmailContent(String emailContent, String byName, String code) {
        /** 已经安装 */
        EopSetting.INSTALL_LOCK = "YES";
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("emailContent", emailContent);
        cfg.setTemplateLoader(stringLoader);
        try {
            Template template = cfg.getTemplate("emailContent", "utf-8");
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("byName", byName);
            root.put("code",code);
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
     * @param email   邮箱
     * @param emailTitle   邮件标题
     */
    private void sendEmail(String emailContent, String email, String emailTitle) {

        EmailModel emailModel = new EmailModel();
        emailModel.setTitle(emailTitle);
        emailModel.setEmail(email);
        emailModel.setContent(emailContent);
        emailSendlManager.sendMqMsg(emailModel);

    }


}
