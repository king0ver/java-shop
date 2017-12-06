package com.enation.app.shop.receipt.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.receipt.model.Receipt;
import com.enation.app.shop.receipt.service.IReceiptManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


/**
 * 发票标签
 * @author whj
 *2013-7-29下午9:45:44
 */
@Component
@Scope("prototype")
public class ReceiptTag extends BaseFreeMarkerTag{
	@Autowired
	private IReceiptManager receiptManager;

	/**
	 * @param receiptId 发票id,int型 必须
	 * @return 发票实体 {@link Receipt}
	 * 如果该订单不存在发票，返回null。
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer receiptId = (Integer) params.get("receiptId");
			 Receipt receipt = receiptManager.getById(receiptId);
			 if(receipt==null){
				receipt = new Receipt();   
			 }
			 return receipt;

	}
	
	
	
}
