package com.enation.app.nanshan.service.impl;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.service.ISettingService;
import com.enation.app.nanshan.service.IWeiXinService;
import com.enation.app.nanshan.util.WeChatConstants;
import com.enation.app.nanshan.util.WeChatFacadeContants;
import com.enation.app.nanshan.vo.BaseResultVo;
import com.enation.app.shop.connect.model.ConnectSetting;
import com.enation.app.shop.connect.utils.HttpUtils;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.spring.SpringContextHolder;

/**
 * 微信服务实现
 * @author jianjianming
 * @version $Id: WeiXinServiceImpl.java,v 0.1 2017年12月26日 下午4:58:48$
 */
@Service("weiXinService")
public class WeiXinServiceImpl implements IWeiXinService {

	@Autowired
	private ICache cache;
	
	protected static ConnectSetting connectSetting;
	
	@Override
	public BaseResultVo sendWeiXinMsg(String tempId, Map<String, Object> params) {
		BaseResultVo result = new BaseResultVo();
		try {
			if(StringUtils.isBlank(tempId)){
				result.setSuccess(false);
				result.setResultCode("-2");
				result.setMessage("模版ID为空.");
				return result;
			}
			String token = getAccessToken(WeChatConstants.TYPE_WEB);
			if(StringUtils.isBlank(token)){
				result.setSuccess(false);
				result.setResultCode("-2");
				result.setMessage("token获取失败.");
				return result;
			}
			String url = WeChatFacadeContants.TEMPLATE_MSG_URL.replace("ACCESS_TOKEN", WeChatConstants.TYPE_WEB);
			JSONObject paramsJson = new JSONObject();
			paramsJson = JSONObject.fromObject(params);
			String jsonStr =  HttpUtils.post(url, paramsJson, "UTF-8");
			JSONObject jsonObj = JSONObject.fromObject(jsonStr);
			// 发送模板消息， 请求返回结果 {"errcode":0,"errmsg":"ok","msgid":200241615}
			if(jsonObj.containsKey("errcode")){
				if("0".equals(jsonObj.get("errcode"))){
					result.setSuccess(true);
					result.setResultCode("0");
					result.setMessage("发送成功.");
				}else{
					result.setSuccess(true);
					result.setResultCode("-1");
					result.setMessage("发送失败.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setResultCode("-1");
			result.setMessage("发送失败.");
		}
		return result;
	}

	@Override
	public String getAccessToken(String type) throws Exception {
		if (connectSetting == null) {
			ISettingService settingService = (ISettingService) SpringContextHolder.getBean("settingService");
			Map<String, Map<String, String>> allSetting = settingService.getSetting();
			if (allSetting.containsKey(ConnectSetting.SETTING_KEY)) {
				connectSetting = fromMap(allSetting.get(ConnectSetting.SETTING_KEY));
			}
		}
		String accessToken = null;
		if ("wechat".equals(type)) {
			accessToken = getAppAccessToken(connectSetting.getWechat_AppId(),connectSetting.getWechat_AppSecret());
		}
		return accessToken;
	}
	
	/**
	 * 根据appid,appsecret 获取验证令牌
	 * 
	 * @param appID
	 * @param appSecret
	 * @return access_token
	 * @throws IOException
	 */
	public synchronized String getAppAccessToken(String appId, String appSecret) throws Exception {
		String wechatCacheToken  = String.valueOf(cache.get(WeChatConstants.WECHAT_TOKEN_KEY));
		if(StringUtils.isNotBlank(wechatCacheToken)&&!"null".equals(wechatCacheToken)){
			return wechatCacheToken;
		}
		String url = String.format(WeChatFacadeContants.GET_TOKEN_URL, appId, appSecret);
		String jsonStr =  HttpUtils.get(url);
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		String wechatToken = null;
		if(jsonObj.containsKey("access_token")){
			wechatToken = jsonObj.getString("access_token");
			cache.put(WeChatConstants.WECHAT_TOKEN_KEY, wechatToken, WeChatConstants.OVERDUE_TOKEN);
		}
		return wechatToken;
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
}
