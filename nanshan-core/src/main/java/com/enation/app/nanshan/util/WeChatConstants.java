package com.enation.app.nanshan.util;


/**
 * 微信常量类
 * @author jianjianming
 * @version $Id: Constants.java,v 0.1 2017年12月26日 下午6:27:33$
 */
public class WeChatConstants {
	
	public static final String TYPE_WEB = "wechat";
	
	/** 微信公众号token缓存key */
	public static final String WECHAT_TOKEN_KEY = "access_token_web";
	/** 缓存时间 */
	public static final int OVERDUE_TOKEN = 60*30000;
	/** 模板边框颜色 */
	public static final String TEMPLATE_BORDER_COLOR = "#00FFFF";
	/** 模板消息字体颜色 */
	public static final String TEMPLATE_FONT_COLOR = "#173177";
}
