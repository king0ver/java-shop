package com.enation.app.shop.message.service.impl;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.enation.app.base.core.model.Smtp;
import com.enation.app.base.core.service.ISendEmailManager;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.service.ISmtpManager;
import com.enation.app.shop.message.service.IEmailSendlManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.util.StringUtil;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * amqp邮箱发送管理类
 * @author dongxin
 * @version v1.0
 * @since v6.4.0
 * 2017年9月27日 下午6:02:03
 */
@Service
public class EmaiSendlManager implements IEmailSendlManager{
	protected final Logger logger = Logger.getLogger(getClass());


	@Autowired
	private ISmtpManager smtpManager;
	@Autowired
	private ISendEmailManager sendEmailManager;

	@Override
	public boolean sendMqMsg(EmailModel emailModel) {
		if(StringUtil.isEmpty(emailModel.getEmail())) {
			return false;
		}
		if(StringUtil.isEmpty(emailModel.getContent())) {
			return false;
		}
		if(StringUtil.isEmpty(emailModel.getTitle())) {
			return false;
		}
		try {
			Smtp smtp = smtpManager.getCurrentSmtp();
			if(smtp.getOpen_ssl()==1||"smtp.qq.com".equals(smtp.getHost())){
				this.sendEmailManager.sendMailByTransport(smtp, emailModel);
			}else{
				this.sendEmailManager.sendMailByMailSender(smtp, emailModel);
			}
			return true;	
		} catch (Exception e) {
			this.logger.error("发送邮件出错",e);
			return false;
		}
	}
	
	

}
