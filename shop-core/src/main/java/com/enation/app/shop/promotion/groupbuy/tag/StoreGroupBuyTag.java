package com.enation.app.shop.promotion.groupbuy.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuy;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 店铺团购标签
 * @author fenlongli
 * @date 2015-7-14 下午11:42:16
 */
@Component
public class StoreGroupBuyTag extends BaseFreeMarkerTag{
	
	@Autowired
	IGroupBuyManager groupBuyManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		if(params.get("gb_id")!=null){
			GroupBuy  groupBuy=groupBuyManager.get((Integer)params.get("gb_id"));
			return groupBuy;
		}
		Integer goodsid =(Integer) params.get("goodsid");
		Integer act_id =(Integer) params.get("act_id");
		GroupBuy  groupBuy=groupBuyManager.getBuyGoodsId(goodsid,act_id);
		return groupBuy;
	}
	

}
