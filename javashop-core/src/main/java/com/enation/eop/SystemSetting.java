package com.enation.eop;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.base.core.service.ISettingService;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;

/**
 * 系统设置
 * @author kingapex
 *
 */
public class SystemSetting {


	private static String static_server_domain; //静态服务器域名
	private static int backend_pagesize; //后台分页数
	private static String  default_img_url; //默认图片路径
	private static String context_path="/"; //虚拟目录 
	private static String global_auth_key="123456";//加密密钥
	private static int static_page_open=0; //是否开启静态页生成
	private static int sms_reg_open=0; //是否开启短信注册
	private static int wap_open=0; //是否开启wap站点
	private static String wap_folder; //wap模板目录 
	private static String wap_domain;//wap站点域名
	private  static int test_mode=0; //是否是测试模式
	/** 主域名 */
	private static String primary_domain;
	/** 是否开启集群 */
	private static int cluster = 0;
	/** 索引目录 */
	private static String index_path;
	/** 静态页生成地址 */
	private static String static_page_address;
	//系统设置中的分组
	public static final String setting_key="system"; 

	//后台菜单列表
	public static final String menuListKey="menuListKey";
	private static ICache cache;

	//系统设置的默认初始化
	static{
		String app_domain= RequestUtil.getDomain();
		static_server_domain = app_domain+"/statics";
		backend_pagesize = 10;
		HttpServletRequest request= ThreadContextHolder.getHttpRequest();
		default_img_url = static_server_domain+"/images/no_picture.jpg";
		if(request!=null)
			context_path =request.getContextPath();

		wap_folder="wap";

		wap_domain=app_domain.replaceAll("www", "m");

	}


	/**
	 * 加载系统设置
	 * 由数据库中加载
	 */
	public static void load(){
		if(cache == null) {
			cache=SpringContextHolder.getBean("cache");
		}
		ISettingService settingService= SpringContextHolder.getBean("settingService");
		Map<String,String> settings = settingService.getSetting(setting_key);
		if(settings==null){
			return ;
		}

		static_server_domain = settings.get("static_server_domain");
		cache.put("static_server_domain", static_server_domain);

		index_path = settings.get("index_path");
		cache.put("index_path", index_path);

		String backend_pagesize_str = settings.get("backend_pagesize");
		backend_pagesize= StringUtil.toInt(backend_pagesize_str,10);
		cache.put("backend_pagesize", backend_pagesize);

		default_img_url = settings.get("default_img_url");
		cache.put("default_img_url", default_img_url);

		context_path = settings.get("context_path");
		cache.put("context_path", context_path);

		global_auth_key =settings.get("global_auth_key");
		cache.put("global_auth_key", global_auth_key);

		String static_page_open_str = settings.get("static_page_open");
		static_page_open= StringUtil.toInt(static_page_open_str,0);
		cache.put("static_page_open", static_page_open);

		String  sms_reg_open_str = settings.get("sms_reg_open");
		sms_reg_open= StringUtil.toInt(sms_reg_open_str,0);
		cache.put("sms_reg_open", sms_reg_open);

		String  wap_open_str = settings.get("wap_open");
		wap_open= StringUtil.toInt(wap_open_str,0);
		cache.put("wap_open", wap_open);

		String  cluster_str = settings.get("cluster");
		cluster= StringUtil.toInt(cluster_str,0);
		cache.put("cluster", cluster);

		primary_domain = settings.get("primary_domain");
		cache.put("primary_domain", primary_domain);

		static_page_address = settings.get("static_page_address");
		cache.put("static_page_address", static_page_address);

		wap_folder =settings.get("wap_folder");
		if( StringUtil.isEmpty(wap_folder ) ){
			wap_folder="wap";
		}
		cache.put("wap_folder", wap_folder);

		wap_domain =settings.get("wap_domain");
		if( StringUtil.isEmpty(wap_domain ) ){
			wap_domain="";
		}
		cache.put("wap_domain", wap_domain);

		//是否开启测试模式
		String  test_mode_str = settings.get("test_mode");
		setTest_mode( StringUtil.toInt(test_mode_str,0) );
		cache.put("test_mode_str",  StringUtil.toInt(test_mode_str,0));
	}


	public static int getBackend_pagesize() {
		return StringUtil.toInt(cache.get("backend_pagesize"), false);
	}

	public static String getStatic_server_domain() {
		if(cache!= null){
			return StringUtil.toString(cache.get("static_server_domain"), false);
		}
		return RequestUtil.getDomain()+"/statics";
	}

	public static String getDefault_img_url() {
		return StringUtil.toString(cache.get("default_img_url"), false);
	}

	public static String getContext_path() {
		return StringUtil.toString(cache.get("context_path"), false);
	}


	public static String getGlobal_auth_key() {
		return StringUtil.toString(cache.get("global_auth_key"), false);
	}


	public static int getStatic_page_open() {
		return StringUtil.toInt(cache.get("static_page_open"), false);
	}



	public static int getSms_reg_open() {
		return StringUtil.toInt(cache.get("sms_reg_open"), false);
	}



	public static int getWap_open() {
		return StringUtil.toInt(cache.get("wap_open"), false);
	}


	public static String getWap_folder() {
		return StringUtil.toString(cache.get("wap_folder"), false);
	}


	public static String getWap_domain() {
		return StringUtil.toString(cache.get("wap_domain"), false);
	}


	public static int getTest_mode() {
		if(cache != null) {
			return StringUtil.toInt(cache.get("test_mode_str"), false);
		}
		return 0;
	}

	public static void setTest_mode(int _test_mode){
		cache.put("test_mode_str", _test_mode);
	}


	public static int getCluster() {
		return StringUtil.toInt(cache.get("cluster"), false);
	}



	public static String getIndex_path() {
		return StringUtil.toString(cache.get("index_path"), false);
	}


	public static String getPrimary_domain() {return StringUtil.toString(cache.get("primary_domain"), false);}


	public static String getStatic_page_address() {return StringUtil.toString(cache.get("static_page_address"), false);}


}
