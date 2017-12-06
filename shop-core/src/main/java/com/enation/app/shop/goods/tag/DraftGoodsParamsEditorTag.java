package com.enation.app.shop.goods.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.vo.GoodsParamsList;
import com.enation.app.shop.goods.service.ICategoryParamsManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.JsonResultUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class DraftGoodsParamsEditorTag extends BaseFreeMarkerTag {
	@Autowired
	private ICategoryParamsManager categoryParamsManager;
	@Autowired
	private ISellerManager sellerManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller member = this.sellerManager.getSeller();
		if(member ==null){
			return JsonResultUtil.getErrorJson("未登录，不能使用此api");
		}
		Integer goods_id = (Integer) params.get("goods_id");
		//商品id是空说明是添加否则为修改
		String cat_id = (String) params.get("catid");
		Integer catid = Integer.valueOf(cat_id);
		//添加model传add，修改传edit
		String model = (String) params.get("model");	
		List<GoodsParamsList> list = null;
		if(model.equals("add")) {
			 list=categoryParamsManager.getParamByCategory(catid);	
			 return list;
		}else {
			 list=categoryParamsManager.getParamByCatAndGoods(catid, goods_id);
			 return list;
		}
	}

}
