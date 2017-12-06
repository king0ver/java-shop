package com.enation.app.shop.promotion.fulldiscount.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 
 * 满优惠促销活动信息标签
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月4日 下午6:01:06
 */
@Component
public class FullDiscountInfoTag extends BaseFreeMarkerTag{
	@Autowired
	private IFullDiscountManager fullDiscountManager;
	
	@Autowired
	private ISellerManager sellerManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String fd_id = request.getParameter("fd_id");
		Seller seller = sellerManager.getSeller();
		FullDiscountVo fullDiscountVo = this.fullDiscountManager.get(Integer.parseInt(fd_id));
		//增加权限
		
		if(fullDiscountVo==null || !fullDiscountVo.getShop_id().equals(seller.getStore_id())){
			throw new UrlNotFoundException();
		}
		
		return fullDiscountVo;
	}
	
}
