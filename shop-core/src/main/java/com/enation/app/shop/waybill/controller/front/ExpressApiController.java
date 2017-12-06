package com.enation.app.shop.waybill.controller.front;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.trade.model.po.Logi;
import com.enation.app.shop.trade.service.ILogiManager;
import com.enation.app.shop.waybill.service.IExpressManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

@RestController
@RequestMapping("/express")
public class ExpressApiController {

	@Autowired
	private IExpressManager expressManager;
	@Autowired
	private ILogiManager logiManager;
	
	/**
	 * 快递查询
	 * @return
	 */
	@RequestMapping(value="/order-kuaidi")
	public ModelAndView orderKuaidi(){
		ModelAndView view = new ModelAndView();

		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String shipno = request.getParameter("ship_no");//物流号
			String logi_id = request.getParameter("logi_id");//物流公司代码
			if(shipno==null || shipno.length()<5){
				Map result = new HashMap();
				result.put("status", "-1");
				Map kuaidiResult = new HashMap();
				kuaidiResult.put("message", "请输入正确的快递单号！\n\r");
				view.addObject("kuaidiResult", kuaidiResult);
				//return JsonResultUtil.getErrorJson("请输入正确的运单号");
			} else {
				Logi logi = logiManager.getLogiById(StringUtil.toInt(logi_id, 0));
				String code = "";
				if(logi == null || logi.getCode()==null){
					code = "yuantong";
				}
				
				Map kuaidiResult = this.expressManager.getDefPlatform(code, shipno);
				kuaidiResult.put("name", logi.getName());
				view.addObject("kuaidiResult", kuaidiResult);
			}
			
		} catch (Exception e) {
			
		}
		if(EopSetting.PRODUCT.equals("b2c")){
			view.setViewName("/themes/kaben/member/order_kuaidi");
		}else{
			view.setViewName("/themes/b2b2cv4/member/order_kuaidi");
		}
		return view;
	}
}
