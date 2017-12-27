package com.enation.app.nanshan.util;

/**
 * 
 * @author jianjianming
 * @version $Id: FacadeContants.java,v 0.1 2017年12月26日 下午5:18:46$
 */
public class WeChatFacadeContants {
	
	/** 发送模板消息URL */
	public static final String TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	/** 获取access_token令牌接口url */
	public static final String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
}
