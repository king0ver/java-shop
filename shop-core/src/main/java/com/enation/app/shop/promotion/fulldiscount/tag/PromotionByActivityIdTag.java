package com.enation.app.shop.promotion.fulldiscount.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.bonus.model.StoreBonusType;
import com.enation.app.shop.promotion.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.framework.cache.ICache;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 根据活动id读取参与的活动信息
 * @author zjp
 * @since v6.4
 * @version v1.0
 */
@Component
public class PromotionByActivityIdTag extends BaseFreeMarkerTag{
	
	@Autowired
	private ICache cache;
	
	@Autowired
	private IB2b2cBonusManager bonusManager;
	
	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager;
	
	@Autowired
	private IFullDiscountManager fullDiscountManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Integer activity_id = (Integer) params.get("activity_id");
		Integer shop_id = (Integer) params.get("shop_id");
		FullDiscountVo fullDiscountVo = fullDiscountManager.get(activity_id);
		Map map = new HashMap();
		map.put("activity", fullDiscountVo);
		if(fullDiscountVo.getIs_send_bonus() == 1) {
			StoreBonusType storeBonusType = bonusManager.get(fullDiscountVo.getBonus_id());
			map.put("bonus", storeBonusType);
		}
		if(fullDiscountVo.getIs_send_gift() == 1) {
			FullDiscountGift fullDiscountGift = fullDiscountGiftManager.get(fullDiscountVo.getGift_id());
			map.put("gift", fullDiscountGift);
		}
		return map;
	}

}
