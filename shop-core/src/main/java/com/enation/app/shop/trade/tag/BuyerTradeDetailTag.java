package com.enation.app.shop.trade.tag;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.trade.model.po.TradePo;
import com.enation.app.shop.trade.model.vo.Trade;
import com.enation.app.shop.trade.service.ITradeQueryManager;
import com.enation.app.shop.trade.support.OrderServiceConstant;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.cache.ICache;
import com.enation.framework.context.webcontext.ThreadContextHolder;

import freemarker.template.TemplateModelException;

/**
 * 买家的交易详细标签
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年8月23日 下午2:25:30
 */
@Component
@Scope("prototype")
public class BuyerTradeDetailTag extends AbstractOrderDetailTag{
 
	@Autowired
	private ITradeQueryManager tradeQueryManager;
	@Autowired
	private ICache cache;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Member member = UserConext.getCurrentMember();
		if(member == null ){
			throw new TemplateModelException("未登陆不能使用此标签[BuyerTradeDetailTag]");
		}
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String tradesn = request.getParameter("tradesn");
		TradePo trade = tradeQueryManager.getOneBySn(tradesn);
		if(trade == null){	
			String cacheKey = OrderServiceConstant.TRADE_SESSION_ID_PREFIX+ tradesn;
			Trade tradeVo = (Trade) cache.get(cacheKey);
			trade = new TradePo(tradeVo);
		}
		
		return trade;
	}

}
