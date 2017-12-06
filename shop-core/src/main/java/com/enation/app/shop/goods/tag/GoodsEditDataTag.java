package com.enation.app.shop.goods.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
import io.swagger.annotations.Api;
/**
 * 
 * 商家中心商品信息标签
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月14日 上午10:08:18
 */
@Component
@Scope("prototype")
@Api(description = "商家中心获取商品基本信息标签")
public class GoodsEditDataTag extends BaseFreeMarkerTag {
	@Autowired
	private IGoodsManager goodsManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Goods goods = goodsManager.getFromDB(Integer.valueOf(params.get("goods_id").toString()));
		//如果不是本店商品 转发到404页面
		if(params.containsKey("store_id")){
			Integer store_id= Integer.valueOf(params.get("store_id").toString());
			if(store_id==null || !store_id.equals(StringUtil.toInt(goods.getSeller_id().toString(),true)) ) {
				this.redirectTo404Html();
			}
		}
		return goods;
	}

}
