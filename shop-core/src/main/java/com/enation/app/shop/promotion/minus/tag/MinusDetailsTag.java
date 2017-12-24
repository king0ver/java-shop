package com.enation.app.shop.promotion.minus.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.minus.model.vo.MinusVo;
import com.enation.app.shop.promotion.minus.service.IMinusManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 单品立减活动详细信息标签
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月24日下午2:34:25
 *
 */
@Component
public class MinusDetailsTag extends BaseFreeMarkerTag {

	@Autowired
	private IMinusManager minusManager;

	@Autowired
	private ISellerManager sellerManager;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.framework.taglib.BaseFreeMarkerTag#exec(java.util.Map)
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {

		HttpServletRequest request = this.getRequest();

		// 获取单品立减活动id
		Integer minus_id = new Integer(request.getParameter("minus_id"));
		if (minus_id.equals(null)) {
			minus_id = Integer.parseInt(params.get("minus_id").toString());
		}

		// 根据id获取单品立减vo
		MinusVo minusVo = this.minusManager.get(minus_id);

		// list中只返回一个MinusVo类的对象
		return minusVo;
	}

}
