package com.enation.app.shop.shop.setting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import com.enation.app.base.core.service.solution.IInstaller;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.goodssearch.service.IGoodsIndexSendManager;
/**
 * 
 * 缓存信息在安装的时候初始化
 * @author zh
 * @version v1.0
 * @since v6.4.0
 * 2017年10月30日 下午5:42:19
 */
@Service
public class CacheInitInstaller implements IInstaller{

	@Autowired 
	private ICategoryManager categoryManager;
	@Autowired
	private IGoodsIndexSendManager  goodsIndexSendManager;

	@Override
	public void install(String productId, Node fragment) {
		if(!"base".equals(productId)){
			return ;
		}
		/**初始化分类缓存*/
		categoryManager.initCategory();
		/** 初始化商品索引 */
		goodsIndexSendManager.startCreate();

	}

}
