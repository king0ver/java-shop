package com.enation.app.shop.message;

import com.enation.app.base.core.model.Member;
import com.enation.app.core.event.IMemberLoginEvent;
import com.enation.app.shop.member.model.vo.MemberLoginMsg;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.message.model.MessageTemplate;
import com.enation.app.shop.message.model.enums.MessageCodeEnum;
import com.enation.app.shop.message.model.enums.MessageStatusEnum;
import com.enation.app.shop.message.model.vo.MobileVo;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.app.shop.message.service.IMemberMessageManager;
import com.enation.app.shop.message.service.IMessageManager;
import com.enation.app.shop.message.service.IMobileSmsSendManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.util.DateUtil;
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
 * 会员登陆成功
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月8日
 */
@Service
public class MemberLoginConsumer implements IMemberLoginEvent{

    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private IMemberManager memberManager;

    @Autowired
    private IMessageManager messageManager;

    @Autowired
    private IMemberMessageManager memberMessageManager;

    @Autowired
    private IMobileSmsSendManager mobileSmsSendManager;

    @Autowired
    private IEmailSendlManager emailSendlManager;

    @Override
    public void memberLogin(MemberLoginMsg memberLoginMsg) {
        /* 获取会员信息 */
        Member member = memberManager.get(memberLoginMsg.getMember_id());
        /* 发送会员消息 */
        memberMessageSend(member);

    }

    private void memberMessageSend(Member member){
        // 记录会员登陆成功信息（会员中心查看）
        MessageTemplate messageTemplate = messageManager.getMessageTemplate(MessageCodeEnum.MEMBERLOGINSUCCESS.name());
        // 判断站内信是否开启
        if(messageTemplate != null) {
        		if (messageTemplate.getNotice_state() == MessageStatusEnum.OPEN.getIndex()) {
                memberMessageManager.addMemberContent(this.replaceContent(messageTemplate.getContent(), member),
                        member.getMember_id());
            }

            // 判断短信是否开启
            if (messageTemplate.getSms_state() == MessageStatusEnum.OPEN.getIndex()) {
                sendMemberSms(this.replaceMobileContent(messageTemplate.getSms_content(), member), member);
            }

            // 判断邮件是否开启
            if (messageTemplate.getEmail_state() == MessageStatusEnum.OPEN.getIndex()) {
                sendEmail(replaceEmailContent(messageTemplate.getEmail_content(), member), member,
                        messageTemplate.getEmail_title());
            }
        }
    }


    /**
     * 替换站内信中的订单内容
     *
     * @param content 站内信
     * @param member 会员实体
     */
    private String replaceContent(String content, Member member) {
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("name", member.getUname());
        valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));

        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        return strSubstitutor.replace(content);
    }

    /**
     * 替换短信中的订单内容
     * @param content 短信
     * @param member 会员实体
     * @return
     */
    private String replaceMobileContent(String content, Member member) {
        /** 已经安装 */
        EopSetting.INSTALL_LOCK = "YES";
        Map<String, Object> valuesMap = new HashMap<String, Object>();
        valuesMap.put("name", member.getUname());
        valuesMap.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd"));
        valuesMap.put("siteName", EopSite.getInstance().getSitename());
        StrSubstitutor strSubstitutor = new StrSubstitutor(valuesMap);
        return strSubstitutor.replace(content);
    }

    /**
     * 发送会员短信
     *
     * @param smsContent 短信内容
     * @param member 会员实体
     */
    private void sendMemberSms(String smsContent, Member member) {
        MobileVo mobileVo = new MobileVo();
        mobileVo.setContent(smsContent);
        mobileVo.setMobile(member.getMobile());
        mobileSmsSendManager.sendMqMsg(mobileVo);
        // 记录日志
        memberMessageManager.addMemberSmsContent(smsContent, member.getMember_id());
    }

    /**
     * 替换邮件内容
     *
     * @param emailContent 邮件信息内容
     * @param member 会员实体
     * @return 邮件内容
     */
    private String replaceEmailContent(String emailContent, Member member) {
        /** 已经安装 */
        EopSetting.INSTALL_LOCK = "YES";
        Configuration cfg = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("emailContent", emailContent);
        cfg.setTemplateLoader(stringLoader);
        try {
            Template template = cfg.getTemplate("emailContent", "utf-8");
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("name", member.getUname());
            root.put("loginTime", DateUtil.toString(DateUtil.getDateline(), "yyyy-MM-dd HH:mm:ss"));
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
     * @param member 会员实体
     * @param emailTitle   邮件标题
     */
    private void sendEmail(String emailContent, Member member, String emailTitle) {
    		if(!StringUtil.isEmpty(member.getEmail())) {
    			EmailModel emailModel = new EmailModel();
    	        emailModel.setTitle(emailTitle);
    	        emailModel.setEmail(member.getEmail());
    	        emailModel.setContent(emailContent);
    	        emailSendlManager.sendMqMsg(emailModel);
    		}

    }

}
