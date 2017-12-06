package com.enation.app.shop.shop.apply.tag;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep1;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep2;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep3;
import com.enation.app.shop.shop.apply.model.vo.ApplyStep4;
import com.enation.app.shop.shop.apply.model.vo.ShopVo;
import com.enation.app.shop.shop.apply.service.IShopManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 店铺申请获取店铺信息标签
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月3日 下午6:35:12
 */
@Component
public class ApplyShopTag extends BaseFreeMarkerTag{
	@Autowired
	private IShopManager shopManager;
	@Override
	protected Object exec(Map params) {
		try {
			Member member = UserConext.getCurrentMember();
			if(member!=null && params.get("step")!=null) {
				ShopVo shopVo = shopManager.getShopVoByMemberId(member.getMember_id());
				String step = params.get("step").toString();
				if(params.get("step").toString().equals("step1")) {
					ApplyStep1 applyStep1 = new ApplyStep1();
					BeanUtils.copyProperties(applyStep1, shopVo);
					return applyStep1;
				}else if(params.get("step").toString().equals("step2")) {
					ApplyStep2 applyStep2 = new ApplyStep2();
					BeanUtils.copyProperties(applyStep2, shopVo);
					return applyStep2;
				}else if(params.get("step").toString().equals("step3")) {
					ApplyStep3 applyStep3 = new ApplyStep3();
					BeanUtils.copyProperties(applyStep3, shopVo);
					return applyStep3;
				}else if(params.get("step").toString().equals("step4")) {
					ApplyStep4 applyStep4 = new ApplyStep4();
					BeanUtils.copyProperties(applyStep4, shopVo);
					return applyStep4;
				}
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
