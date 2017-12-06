package com.enation.app.shop.shop.seller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.shop.setting.model.po.StoreLevel;
import com.enation.app.shop.shop.setting.service.IStoreLevelManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 店铺等级信息Tag
 * @author LiFenLong
 *
 */
public class StoreLevelInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private IStoreLevelManager storeLevelManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		try {
			//获取店铺等级
			Integer level_id = Integer.parseInt(params.get("store_level").toString());
			//查询店铺等级信息
			StoreLevel storeLevel = storeLevelManager.getStoreLevel(level_id);
			//查询下一等铺等级信息
			StoreLevel nextstoreLevel = storeLevelManager.getStoreLevel(level_id+1);
			Map map = new HashMap();
			map.put("space_capacity", storeLevel.getSpace_capacity());
			//判断店铺等级是否为最高级
			if(nextstoreLevel!=null){
				//如果店铺等级不是最高级则将下一级店铺信息存入
				map.put("next_space_capacity", nextstoreLevel.getSpace_capacity());
			}
			return map;
		} catch (Exception e) {
			throw new UrlNotFoundException();
		}
		
		
	}
	
}
