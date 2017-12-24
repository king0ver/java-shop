package com.enation.app.shop.trade.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.promotion.fulldiscount.model.po.FullDiscountGift;
import com.enation.app.shop.trade.model.po.OrderMeta;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import freemarker.template.TemplateModelException;

/**
 * 
 * 会员中心和商家中心的赠品详情标签
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月12日 下午6:48:41
 */
@Component
@Scope("prototype")
public class OrderGiftTag extends BaseFreeMarkerTag {
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		List<Object> term = new ArrayList<Object>();
		StringBuffer sqlBuffer = new StringBuffer();
		String ordersn = request.getParameter("ordersn");
		if (ordersn == null) {
			ordersn = (String) params.get("ordersn");
		}
		String status = request.getParameter("status");
		if (status == null) {
			status = (String) params.get("status");
		}
		Gson gson = new Gson();
		sqlBuffer.append("select meta_value from es_order_meta where meta_key='gift'");
		if (!StringUtil.isEmpty(ordersn)) {
			sqlBuffer.append(" and order_sn=? ");
			term.add(ordersn);
		}
		if (!StringUtil.isEmpty(status)) {
			sqlBuffer.append(" and status=? ");
			term.add(status);
		}
		List<FullDiscountGift> giftlist = null;
		List<OrderMeta> list = daoSupport.queryForList(sqlBuffer.toString(), OrderMeta.class, term.toArray());
		for (OrderMeta metas : list) {
			if (metas.getMeta_value() != null) {
				String meta = metas.getMeta_value();
				giftlist = gson.fromJson(meta, new TypeToken<List<FullDiscountGift>>() {
				}.getType());
			}
			break;
		}
		if (giftlist == null) {
			giftlist = new ArrayList<FullDiscountGift>();
		}
		return giftlist;
	}

}
