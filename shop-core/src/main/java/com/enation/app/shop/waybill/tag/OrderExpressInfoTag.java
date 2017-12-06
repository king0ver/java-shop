package com.enation.app.shop.waybill.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.model.po.Logi;
import com.enation.app.shop.trade.service.ILogiManager;
import com.enation.app.shop.waybill.service.IExpressManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 
 * 快递信息标签
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年10月16日 下午5:48:49
 */
@Component
public class OrderExpressInfoTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IExpressManager expressManager;
	@Autowired
	private ILogiManager logiManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String shipno = (String) params.get("ship_no");//物流号
		String logi_id = (String) params.get("logi_id");//物流公司代码
		
		if(shipno==null || shipno.length()<5){
			Map result = new HashMap();
			result.put("status", "-1");
			Map kuaidiResult = new HashMap();
			kuaidiResult.put("message", "请输入正确的快递单号！\n\r");
			return kuaidiResult;
		} else {
			Logi logi = logiManager.getLogiById(StringUtil.toInt(logi_id, 0));
			String code = logi.getCode();
			if(logi == null || logi.getCode()==null){
				code = "yuantong";
			}
			Map kuaidiResult = this.expressManager.getDefPlatform(code, shipno);
			kuaidiResult.put("name", logi.getName());
			return kuaidiResult;
		}
	}

}
