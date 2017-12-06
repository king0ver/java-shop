package com.enation.app.shop.connect.service;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.shop.connect.model.ConnectSetting;
import com.enation.app.shop.connect.model.ConnectUser;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;
import com.qq.connect.utils.QQConnectConfig;

import org.apache.commons.lang3.StringUtils;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Author: Dawei Datetime: 2016-03-04 16:28
 */
public abstract class ConnectLogin {

	protected ServletRequest request;

	protected static ConnectSetting connectSetting;

	public ConnectLogin(ServletRequest request) {
		this.request = request;
		initConnectSetting();
	}

	/**
	 * 初始化信息登录的参数
	 */
	private void initConnectSetting() {
		if (connectSetting == null) {
			ISettingService settingService = (ISettingService) SpringContextHolder.getBean("settingService");
			Map<String, Map<String, String>> allSetting = settingService.getSetting();
			if (allSetting.containsKey(ConnectSetting.SETTING_KEY)) {
				connectSetting = fromMap(allSetting.get(ConnectSetting.SETTING_KEY));
			}
		}
		if (!StringUtils.isEmpty(connectSetting.getQq_AppId())) {
			QQConnectConfig.updateAppIdAndKey(connectSetting.getQq_AppId(), connectSetting.getQq_AppKey());

			if (isMobile()) {
				QQConnectConfig.updateRedirectUri(connectSetting.getQq_wap_RedirectUri());
			} else {
				QQConnectConfig.updateRedirectUri(connectSetting.getQq_RedirectUri());
			}
		}
		if (!StringUtils.isEmpty(connectSetting.getWeibo_AppKey())) {
			weibo4j.util.WeiboConfig.updateAppIdAndKey(connectSetting.getWeibo_AppKey(),
					connectSetting.getWeibo_AppSecret());
			if (isMobile()) {
				weibo4j.util.WeiboConfig.updateRedirectUri(connectSetting.getWeibo_wap_RedirectUri());
			} else {
				weibo4j.util.WeiboConfig.updateRedirectUri(connectSetting.getWeibo_RedirectUri());
			}
		}
		return;
	}

	private static boolean isMobile() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		if (request == null)
			return false;
		String user_agent = request.getHeader("User-Agent");
		if (StringUtil.isEmpty(user_agent))
			return false;

		String userAgent = user_agent.toLowerCase();

		if (userAgent.contains("android") || userAgent.contains("iphone")) {
			return true;
		}
		return false;
	}

	private ConnectSetting fromMap(Map<String, String> setting) {
		ConnectSetting connectSetting = new ConnectSetting();
		if (setting == null)
			return connectSetting;

		// QQ
		if (setting.containsKey("qq_appid")) {
			connectSetting.setQq_AppId(setting.get("qq_appid"));
		}
		if (setting.containsKey("qq_appkey")) {
			connectSetting.setQq_AppKey(setting.get("qq_appkey"));
		}
		if (setting.containsKey("qq_redirecturi")) {
			connectSetting.setQq_RedirectUri(setting.get("qq_redirecturi"));
		}
		if (setting.containsKey("qq_wap_redirecturi")) {
			connectSetting.setQq_wap_RedirectUri(setting.get("qq_wap_redirecturi"));
		}

		// 微博
		if (setting.containsKey("weibo_appkey")) {
			connectSetting.setWeibo_AppKey(setting.get("weibo_appkey"));
		}
		if (setting.containsKey("weibo_appsecret")) {
			connectSetting.setWeibo_AppSecret(setting.get("weibo_appsecret"));
		}
		if (setting.containsKey("weibo_redirecturi")) {
			connectSetting.setWeibo_RedirectUri(setting.get("weibo_redirecturi"));
		}
		if (setting.containsKey("weibo_wap_redirecturi")) {
			connectSetting.setWeibo_wap_RedirectUri(setting.get("weibo_wap_redirecturi"));
		}

		// 微信
		if (setting.containsKey("wechat_appid")) {
			connectSetting.setWechat_AppId(setting.get("wechat_appid"));
		}
		if (setting.containsKey("wechat_appsecret")) {
			connectSetting.setWechat_AppSecret(setting.get("wechat_appsecret"));
		}
		if (setting.containsKey("wechat_redirecturi")) {
			connectSetting.setWechat_RedirectUri(setting.get("wechat_redirecturi"));
		}
		if (setting.containsKey("wechat_wap_redirecturi")) {
			connectSetting.setWechat_wap_RedirectUri(setting.get("wechat_wap_redirecturi"));
		}

		// 微信公众号
		if (setting.containsKey("wechatMp_appid")) {
			connectSetting.setWechatMp_AppId(setting.get("wechatMp_appid"));
		}
		if (setting.containsKey("wechatMp_appsecret")) {
			connectSetting.setWechatMp_AppSecret(setting.get("wechatMp_appsecret"));
		}
		if (setting.containsKey("wechatMp_redirecturi")) {
			connectSetting.setWechatMp_RedirectUri(setting.get("wechatMp_redirecturi"));
		}
		if (setting.containsKey("wechatMp_wap_redirecturi")) {
			connectSetting.setWechatMp_wap_RedirectUri(setting.get("wechatMp_wap_redirecturi"));
		}

		return connectSetting;
	}

	/**
	 * 重新配置connectSetting
	 */
	public static void resetConnectSetting() {
		connectSetting = null;
	}

	/**
	 * 获取授权登录的url
	 *
	 * @return
	 */
	public abstract String getLoginUrl();

	/**
	 * 登录成功后的回调方法
	 *
	 * @return
	 */
	public abstract ConnectUser loginCallback();

}
