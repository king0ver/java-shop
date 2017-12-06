package com.enation.app.shop.promotion.tool.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 根据goodsid读取参与的活动集合
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 */
@Component
public class PromotionListByGoodsIdTag extends BaseFreeMarkerTag{

	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Integer goods_id = (Integer) params.get("goods_id");
		List<PromotionGoodsVo> list = this.promotionGoodsManager.getPromotion(goods_id);
		return list;
	}

}
