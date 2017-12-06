package com.enation.app.shop.promotion.fulldiscount.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.promotion.fulldiscount.model.vo.StoreActivityGift;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountGiftManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.sdk.utils.StaticResourcesUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 
 * 获取满优惠促销赠品信息
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月5日 下午7:15:37
 */
@Component
public class FullDiscountGiftTag extends BaseFreeMarkerTag{

	@Autowired
	private IFullDiscountGiftManager fullDiscountGiftManager;
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String gift_id = request.getParameter("gift_id");
		
		//如果赠品id为空则是从标签中传递过来的
		if(gift_id == null && StringUtil.isEmpty(gift_id)){
			if(params.get("gift_id")!=null){
				gift_id = (String) params.get("gift_id");
			}else{
				return new StoreActivityGift();
			}
		}
		
		FullDiscountGift fullDiscountGift = this.fullDiscountGiftManager.get(Integer.parseInt(gift_id));
		
		fullDiscountGift.setGift_img(StaticResourcesUtil.convertToUrl(fullDiscountGift.getGift_img()));
		
		return fullDiscountGift;
	}

}
