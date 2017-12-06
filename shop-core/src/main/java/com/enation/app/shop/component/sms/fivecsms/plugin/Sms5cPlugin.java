package com.enation.app.shop.component.sms.fivecsms.plugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.sms.AbstractSmsPlugin;
import com.enation.app.base.core.plugin.sms.ISmsSendEvent;

/**
 * 
 * 5c短信组件
 * @author xulipeng
 * @version v1.0
 * @since v1.0
 * 2015年06月09日16:38:28
 */
@Component
public class Sms5cPlugin extends AbstractSmsPlugin implements ISmsSendEvent {

	@Override
	public boolean onSend(String phone, String content, Map param) {
		try {

			// 创建StringBuffer对象用来操作字符串
			StringBuffer sb = new StringBuffer("http://m.5c.com.cn/api/send/?");

			// APIKEY
			sb.append("apikey=" + param.get("apikey"));

			// 用户名
			sb.append("&username=" + param.get("username"));

			// 向StringBuffer追加密码
			sb.append("&password=" + param.get("password"));

			// 向StringBuffer追加手机号码
			sb.append("&mobile=" + phone);

			// 向StringBuffer追加消息内容转URL标准码
			sb.append("&content=" + URLEncoder.encode(content, "GBK"));

			// 创建url对象
			URL url = new URL(sb.toString());
			
			// 打开url连接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// 设置url请求方式 ‘get’ 或者 ‘post’
			connection.setRequestMethod("POST");

			// 发送
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

			// 返回发送结果
			String inputline = in.readLine();
			
			if(!inputline.startsWith("success")){
				throw new RuntimeException(inputline);
			}else{
				return true;
			}

		} catch (Exception e) {

		}
		return false;
	}

	@Override
	public String getId() {
		return "sms5cPlugin";
	}

	@Override
	public String getName() {
		return "5c网关短信插件";
	}

}
