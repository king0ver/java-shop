package com.enation.app.shop.member.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.member.model.MemberAddress;
import com.enation.app.shop.member.service.IMemberAddressManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 读取当前会员收货地址列表
 * @author kingapex
 *2013-7-26下午2:48:02
 */
@Component
@Scope("prototype")
public class MemberAddressTag extends BaseFreeMarkerTag {
	@Autowired
	private IMemberAddressManager memberAddressManager;
	@Autowired
	private ISellerManager sellerManager;
	
	/**
	 * 读取当前会员收货地址列表
	 * @param 无
	 * @return 收货地址列表
	 * {@link MemberAddress}
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Seller member=sellerManager.getSeller();
		if(member==null){
			throw new TemplateModelException("未登陆不能使用此标签[ConsigneeListTag]");
		}
		return memberAddressManager.listAddress();
	}

}
