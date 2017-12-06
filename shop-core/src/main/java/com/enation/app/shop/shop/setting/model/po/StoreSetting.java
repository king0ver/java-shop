package com.enation.app.shop.shop.setting.model.po;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.ISettingService;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 店铺设置
 * 
 * @author Kanon 2016-2-7
 * @version 1.0
 * @since 3.0
 */
@Component
public class StoreSetting {
	public static final String setting_key = "store"; // 系统设置中的分组

	private static Integer auth = 0; // 商家商品是否审核、 0：不需要审核，1：需要审核

	private static Integer edit_auth = 0; // 商品修改是否审核、 0：不需要审核，1：需要审核

	private static Integer self_auth = 0; // 自营商品是否审核、 0：不需要审核，1：需要审核

	private static String store_themes = "/store_themes/themes/"; // 店铺模板路径

	private static String shop_wap_themes = "/store/themes/";


	private static ICache cache;
	public StoreSetting() {
	}

	@Autowired
	private ISettingService settingService;


	/**
	 * 加载店铺设置 由数据库中加载
	 */
	public void load() {
		if(cache == null) {
			cache=SpringContextHolder.getBean("cache");
		}
		Map<String, String> settings = settingService.getSetting(setting_key);
		auth = Integer.parseInt(settings.get("auth"));
		cache.put("auth", auth);
		edit_auth = Integer.parseInt(settings.get("edit_auth"));
		cache.put("edit_auth", edit_auth);
		self_auth = Integer.parseInt(settings.get("self_auth"));
		cache.put("self_auth", self_auth);
		store_themes = settings.get("store_themes");
		cache.put("store_themes", store_themes);
		shop_wap_themes = settings.get("shop_wap_themes");
		cache.put("shop_wap_themes", shop_wap_themes);
	}

	public static Integer getAuth() {
		return StringUtil.toInt(cache.get("auth"), false);
	}

	public static Integer getEdit_auth() {
		return StringUtil.toInt(cache.get("edit_auth"), false);
	}

	public static Integer getSelf_auth() {
		return StringUtil.toInt(cache.get("self_auth"), false);
	}

	public static String getStore_themes() {
		return StringUtil.toString(cache.get("store_themes"), false);
	}

	public static String getShop_wap_themes() {
		return StringUtil.toString(cache.get("shop_wap_themes"), false);
	}
}
