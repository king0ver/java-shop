package com.enation.app.shop.trade.tag;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.trade.service.ICheckoutParamManager;
import com.enation.app.shop.trade.support.CheckoutParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.member.model.MemberAddress;
import com.enation.app.shop.member.service.IMemberAddressManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 会员的默认收货地址获取标签
 * 为结算页提供使用：
 * 1、选择从结算参数获取收货地址id，如果有，则使用此id
 * 2、否则读取会员的默认收货地址id，使用此id
 * 3、则则读取第一个会员的收货地址
 * 4、否则返回null
 * 5、如果结算参数中没有收货人，且找到了默认收货人，则给结算参数设置为这个默认收货人
 * @author kingapex
 */

@Component
public class DefaultConsigneeTag extends BaseFreeMarkerTag{

	@Autowired
	private IMemberAddressManager memberAddressManager;

	@Autowired
	private ICheckoutParamManager checkoutParamManager;

	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = UserConext.getCurrentMember();

		if(member == null){
			return null;
		}else{
			int memberid =member.getMember_id();

			CheckoutParam checkoutParam =  checkoutParamManager.getParam();
			if(checkoutParam != null ){

				Integer addressid  = checkoutParam.getAddressId();
				MemberAddress address = memberAddressManager.getAddress(addressid);
				if(address!=null){

					//因为存放在redis中，有可能不是这个人的收货人
					if(memberid == address.getMember_id() ) {
						return address;
					}
				}

			}



			//走到这里说明结算参数中获取收货地址失败了，那么读取会员的默认收货人
			MemberAddress address = memberAddressManager.getMemberDefault(memberid);
			if(address!=null){
				//给结算参数设置默认收货人id
				checkoutParamManager.setAddressId(address.getAddr_id());
				return  address;
			}

			//走到这里读取会员的默认收货地址失败了，那么读取会员的第一个收货地址
			List<MemberAddress>  addressList =  memberAddressManager.listAddress(memberid);
			if (!addressList.isEmpty()){
				address = addressList.get(0);
				if(address!=null){
					//给结算参数设置默认收货人id
					checkoutParamManager.setAddressId(address.getAddr_id());
					return  address;
				}
			}

			return  null;

		}

	}

}
