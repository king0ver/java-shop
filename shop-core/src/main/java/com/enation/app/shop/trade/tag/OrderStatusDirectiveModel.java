package com.enation.app.shop.trade.tag;

import java.io.IOException;
import java.util.Map;

import com.enation.app.shop.trade.model.enums.OrderStatus;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
/**
 * 
 * 前台订单状态指令
 * @author    kanon
 * @version   1.0.0,2016年7月28日
 * @since     v6.1
 */
public class OrderStatusDirectiveModel implements TemplateDirectiveModel {

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,TemplateDirectiveBody body) throws TemplateException, IOException {
		String status=  params.get("status").toString();
		String type= params.get("type").toString();
		if("order".equals(type)){
			String text  = OrderStatus.valueOf(status).getDescription();
			env.getOut().write(text);
		}
	}

	public static void main(String[] args){
		
		//System.out.println(2.0-1.9);  
	}
}
