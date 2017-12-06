package com.enation.app.shop.promotion.bonus.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.bonus.model.StoreBonusType;
import com.enation.app.shop.promotion.bonus.service.IB2b2cBonusManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺所有有效的优惠券集合Tag
 * @author DMRain
 * @date 2016年1月19日
 * @version v1.0
 * @since v1.0
 */
@Component
public class BonusListTag extends BaseFreeMarkerTag{
	@Autowired
	private IB2b2cBonusManager b2b2cBonusManager;
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller member = this.sellerManager.getSeller();
		List<StoreBonusType> bonusList = this.b2b2cBonusManager.getList(member.getStore_id());
		return bonusList;
	}
}
