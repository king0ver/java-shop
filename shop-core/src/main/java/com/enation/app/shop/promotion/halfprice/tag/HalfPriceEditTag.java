package com.enation.app.shop.promotion.halfprice.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.halfprice.model.vo.HalfPriceVo;
import com.enation.app.shop.promotion.halfprice.service.IHalfPriceManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取第二件半价促销活动信息Tag
 * @author DMRain
 * @date 2015年12月30日
 * @version v1.0
 * @since v1.0
 */
@Component
public class HalfPriceEditTag extends BaseFreeMarkerTag{

	@Autowired
	private IHalfPriceManager halfPriceManager;
	@Autowired
	private ISellerManager storeMemberManager;
	
	
	/**
	 * 获取第二件半价促销活动信息Tag
	 * params 传入页面的的值
	 * return halfPriceVo 第二件半价促销活动信息实体
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String activity_id = request.getParameter("activity_id");
		Seller member = storeMemberManager.getSeller();
		HalfPriceVo halfPriceVo = this.halfPriceManager.get(Integer.parseInt(activity_id));
		//增加权限
		
		if(halfPriceVo==null || !halfPriceVo.getShop_id().equals(member.getStore_id())){
			throw new UrlNotFoundException();
		}
		
		return halfPriceVo;
	}

}
