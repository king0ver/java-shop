package com.enation.app.shop.aftersale.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.aftersale.model.vo.RefundDetail;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import freemarker.template.TemplateModelException;

/**
 * 退款退货详细tag
 * @author fk
 * @version 6.4
 * 2017年8月3日16:21:24
 */
@Component
public class RefundDetailTag extends BaseFreeMarkerTag {

	@Autowired
	private IAfterSaleManager afterSaleManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member =  UserConext.getCurrentMember();

		if (member == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sn = (String) params.get("sn");
		RefundDetail detail = this.afterSaleManager.getDetail(sn);
		
		//验证是否当前会员的退款单
		Integer member_id  = detail.getRefund().getMember_id();
		if(member_id.compareTo( member.getMember_id() )!=0){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		
		return detail;
	}

}
