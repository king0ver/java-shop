package com.enation.app.shop.payment.tag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.ConfigItem;
import com.enation.app.shop.component.payment.plugin.weixin.WeixinPayPlugin;
import com.enation.app.shop.component.payment.plugin.weixin.WeixinUtil;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import freemarker.template.TemplateModelException;
import net.sf.json.JSONObject;

/**
 * 获取微信openId Tag
 * v2.0，去除自动登录
 * @author Sylow
 * @version v2.0,2016年7月11日
 * @since v6.1
 */
@SuppressWarnings("deprecation")
@Component
public class OpenIdGetTag extends BaseFreeMarkerTag {
	
	@Autowired
	private ICache cache;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String ua = request.getHeader("user-agent").toLowerCase();
		if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器  
			//判断是否已经获取过openid
			String openid = (String) cache.get(WeixinPayPlugin.OPENID_SESSION_KEY+request.getSession().getId());
			if(!StringUtil.isEmpty(openid)){
				return "";
			}
			String code = this.getRequest().getParameter("code");
			if(StringUtil.isEmpty(code)){ //如果没有接收到微信code则跳转到授权页面
				String oauthurl = getOAuth2Url();
				try {
					//System.out.println("跳");
					ThreadContextHolder.getHttpResponse().sendRedirect(oauthurl);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return "";
			}else{
				Map<String,String> cfgparams = this.getConfig();
				
				String appid = cfgparams.get("appid");
				String appsecret = cfgparams.get("appSecret");
				
				String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+appsecret+"&code="+code+"&grant_type=authorization_code";
				String weixinJson = this.httpget(url);
				openid = JSONObject.fromObject(weixinJson).get("openid").toString();
				//System.out.println(openid);
				cache.put(WeixinPayPlugin.OPENID_SESSION_KEY+request.getSession().getId(), openid,6000);
			}
		} 
		
		return "";
	}
	
	/**
	 * 获取插件的配置方式
	 * @return
	 */
	protected Map<String, String> getConfig(){
		//获取当前支付插件的id
		String paymentMethodId = "weixinPayPlugin";
		String config  = daoSupport.queryForString("select config from es_payment_method where plugin_id=?", paymentMethodId);
		if(StringUtil.isEmpty(config)){
			return new HashMap<>();
		}
		Gson gson = new Gson();
		List<ConfigItem> list = gson.fromJson(config,   new TypeToken<List<ConfigItem>>() {  
                }.getType());
		Map<String, String> result = new HashMap<>();
		if(list!=null){
			for(ConfigItem item : list){
				result.put(item.getName(), item.getValue());
			}
		}
		return result;
	}
	
	
	private String getOAuth2Url(  ){
		
		Map<String,String> cfgparams = this.getConfig();
		if (cfgparams == null) {
			return "/500.html";
		}
		String appid = cfgparams.get("appid");

		String url = WeixinUtil.getWholeUrl();
		try {
			url = URLEncoder.encode(url,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		url= "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appid+"&redirect_uri="+url+"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		return url; 
	}
	

	private String httpget(String uri) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(uri);
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "utf-8");
		 
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		return "error";
	}
	
}
