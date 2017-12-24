package com.enation.app.shop.shop.seller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.setting.model.po.ShipTemplate;
import com.enation.app.shop.shop.setting.service.IShipTemplateManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
 
/**
 * 店铺配送模版标签 
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年9月25日 下午5:07:39 
 *
 */
@Component
public class SelectShipTplTag extends BaseFreeMarkerTag{

	@Autowired
	private IShipTemplateManager shipTemplateManager;
	@Autowired
	private ISellerManager sellerManager;
	
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller seller = sellerManager.getSeller();
		if(seller == null ) {
			return null;
		}
		ShipTemplate template = shipTemplateManager.getOne(Integer.parseInt(params.get("tpl_id").toString()));
		
		if(template.getArea_json()==null){
			template.setArea_json("[]");
		}
		return template;
	}

}
