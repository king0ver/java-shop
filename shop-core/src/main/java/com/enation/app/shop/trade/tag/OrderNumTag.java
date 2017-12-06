package com.enation.app.shop.trade.tag;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.comments.service.IStoreMemberCommentManager;
import com.enation.app.shop.trade.model.vo.OrderLine;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.model.vo.TradeLine;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import freemarker.template.TemplateModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 获得该会员订单在各个状体下的个数
 * @author wanghongjun    2015-04-17
 *
 */
@Component
@Scope("prototype")
public class OrderNumTag extends BaseFreeMarkerTag{
	@Autowired
	private IOrderQueryManager orderQueryManager;
	
	@Override
	public Object exec(Map params) throws TemplateModelException {
		Member member = UserConext.getCurrentMember();
		if(member.getMember_id() != null){
			OrderQueryParam queryParam = new OrderQueryParam();
			queryParam.setMember_id(member.getMember_id());
			Map<Object,Integer> map = this.orderQueryManager.getOrderNum(queryParam);
			return map;
		}
		return 0;

	}

}
