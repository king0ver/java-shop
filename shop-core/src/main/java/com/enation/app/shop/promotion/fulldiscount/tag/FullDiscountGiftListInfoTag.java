package com.enation.app.shop.promotion.fulldiscount.tag;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 获取满优惠促销赠品集合标签
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午7:33:06
 */
@Component
public class FullDiscountGiftListInfoTag  extends BaseFreeMarkerTag{

	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager;
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller member = this.sellerManager.getSeller();
		List<FullDiscountGift> giftList = this.fullDiscountGiftManager.listAll(member.getStore_id());
		return giftList;
	}

}
