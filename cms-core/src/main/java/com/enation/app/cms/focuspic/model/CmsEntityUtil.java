package com.enation.app.cms.focuspic.model;

import javax.servlet.http.HttpServletRequest;

import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;

/**
 * 
 * CMSEntityUtil 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月14日 下午1:19:28
 */
public class CmsEntityUtil {
	
	/**
	 * 生成Navigator的url
	 * @param navigator
	 */
	public static String createOperationUrl(String operation_type,String operation_param,String client_type){
		
		String goodspage = "goods_list";
		if("mobile".equals(client_type)){
			goodspage = "goods-list";
		}
		if("none".equals(operation_type)){
			return "";
		}
		if("keyword".equals(operation_type)){
			return getContextPath()+"/"+goodspage+".html?keyword="+operation_param;
		}
		IDaoSupport daoSupport = SpringContextHolder.getBean("daoSupport");
		if("goods-sn".equals(operation_type)){
			// 因sn由卖家定义，所以不再是唯一的，sn有时会获取多个id，导致报错，这里将直接获取商品id
			// Integer goods_id = daoSupport.queryForInt("select goods_id from es_goods where sn = ?", operation_param);
			return getContextPath()+"/goods-"+operation_param+".html";
		}
		if("goods-cat".equals(operation_type)){
			return getContextPath()+"/"+goodspage+".html?cat="+operation_param;
		}
		
		return "";
	}
	
	/**
	 * 得到服务的ContextPath
	 * @return
	 */
	private static String getContextPath(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName = request.getContextPath();
		return serverName;
	}
	
	
}
