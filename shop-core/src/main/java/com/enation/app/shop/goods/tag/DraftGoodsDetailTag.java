package com.enation.app.shop.goods.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 
 * 商家中心获取草稿箱商品信息
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 下午2:11:08
 */
@Component
@Scope("prototype")
public class DraftGoodsDetailTag extends BaseFreeMarkerTag {
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		DraftGoods goods = draftGoodsManager.get(Integer.valueOf(params.get("goods_id").toString()));
		// 如果不是本店商品 转发到404页面
		if (params.containsKey("store_id")) {
			Integer store_id = Integer.valueOf(params.get("store_id").toString());
			if (store_id == null || !store_id.equals(StringUtil.toInt(goods.getSeller_id().toString(), true))) {
				this.redirectTo404Html();
			}
		}
		return goods;
	}

}
