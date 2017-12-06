package com.enation.app.shop.goods.tag;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.shop.trade.service.ITransactionRecordManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
/**
 * 成交记录标签
 * @author LiFenLong
 *
 */
public class RecordListTag extends BaseFreeMarkerTag{
	
	@Autowired
	private ITransactionRecordManager transactionRecordManager;
	/**
	 * @param goods_id,商品Id
	 * @return
	 */
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
		Integer pageNo=this.getPage();
		Integer pageSize= this.getPageSize();
		
		return transactionRecordManager.get(pageNo, pageSize, goods_id);
	}
	
}
