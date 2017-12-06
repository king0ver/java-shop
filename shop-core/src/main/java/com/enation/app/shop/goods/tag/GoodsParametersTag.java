package com.enation.app.shop.goods.tag;

import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.ICategoryParamsManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 商品详情页的商品详情，也就是商品的参数
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月26日 上午10:43:13
 */
@Component
@Scope("prototype")
public class GoodsParametersTag extends BaseFreeMarkerTag {

	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private ICategoryParamsManager categoryParamsManager;
	@SuppressWarnings({"rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		GoodsVo good =this.goodsManager.getFromCache(Integer.parseInt(params.get("goodsid").toString()));
		Integer category_id = good.getCategory_id();
		List<GoodsParamsList> list = null;
		list=categoryParamsManager.getParamByCatAndGoods(category_id, Integer.parseInt(params.get("goodsid").toString()));
		return list;
		
	}

}
