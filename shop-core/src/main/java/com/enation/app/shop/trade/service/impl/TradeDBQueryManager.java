package com.enation.app.shop.trade.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.enums.OrderFrontStatus;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.TradePo;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.model.vo.TradeLine;
import com.enation.app.shop.trade.service.ITradeQueryManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;


/**
 * 交易查询业务类
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月6日下午8:12:58
 */
@Service
public class TradeDBQueryManager implements ITradeQueryManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	
	@Override
	public TradePo getOneBySn(String tradesn) {
		String sql ="select * from es_trade where trade_sn=?";
		TradePo po = this.daoSupport.queryForObject(sql, TradePo.class, tradesn);
		return po;
	}


	@Override
	public Page<TradeLine> queryMyTrade(OrderQueryParam queryParam) {
		StringBuffer sql = new StringBuffer(
				"select * from es_trade t  where trade_sn in ( select trade_sn from es_order o where o.disabled=0 ");
		List<Object> term = new ArrayList<Object>();

		// 按买家查询
		Integer memberid = queryParam.getMember_id();
		if (memberid != null) {
			sql.append(" and o.member_id=?");
			term.add(memberid);
		}

		// 按订单编号查询
		if (StringUtil.notEmpty(queryParam.getOrder_sn())) {
			sql.append(" and o.sn=?");
			term.add(queryParam.getOrder_sn());
		}

		// 按交易编号查询
		if (StringUtil.notEmpty(queryParam.getTrade_sn())) {
			sql.append(" and o.trade_sn=?");
			term.add(queryParam.getTrade_sn());
		}

		// 按时间查询
		String start_time = queryParam.getStart_time();
		String end_time = queryParam.getEnd_time();
		if (StringUtil.notEmpty(start_time)) {
			sql.append(" and o.create_time >= ?");
			term.add(DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		}

		if (StringUtil.notEmpty(end_time)) {
			sql.append(" and o.create_time <= ?");
			term.add(DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));
		}

		// 按购买人用户名
		String memberName = queryParam.getBuyer_name();
		if (StringUtil.notEmpty(memberName)) {
			sql.append(" and o.member_name = ?");
			term.add(memberName);
		}

		// 按标签查询
		String tag = queryParam.getTag();
		if (!StringUtil.isEmpty(tag)) {

			// 待发货
			if (tag.equals(OrderFrontStatus.wait_ship.name())) {
				sql.append(" and( ( o.payment_type!='cod'  and  o.order_status='" + OrderStatus.PAID_OFF + "')  ");// 非货到付款的，要已结算才能发货
				sql.append(" or ( o.payment_type='cod' and  o.order_status='" + OrderStatus.CONFIRM + "')) ");// 货到付款的，已确认就可以发货

				// 待付款
			} else if (tag.equals(OrderFrontStatus.wait_pay.name())) {
				sql.append(" and ( ( o.payment_type!='cod' and  o.order_status='" + OrderStatus.CONFIRM + "') ");// 非货到付款的，未付款状态的可以结算
				sql.append(" or ( o.payment_type='cod' and   o.order_status='" + OrderStatus.ROG + "'  ) )");// 货到付款的要发货或收货后才能结算

				// 待收货
			} else if (tag.equals(OrderFrontStatus.wait_rog.name())) {
				sql.append(" and o.order_status='" + OrderStatus.SHIPPED + "'");
			}

		}

		if (StringUtil.notEmpty(queryParam.getBuyer_name())) {
			sql.append(" and o.items_json like ?");
			term.add("%" + queryParam.getGoods_name() + "%");
		}

		// 按商品名称查询
		if (StringUtil.notEmpty(queryParam.getGoods_name())) {
			sql.append(" and o.items_json like ?");
			term.add("%" + queryParam.getGoods_name() + "%");
		}
		sql.append(" )");
		sql.append(" order by t.create_time desc");
		Page<TradeLine> page = daoSupport.queryForPage(sql.toString(), queryParam.getPage_no(),
				queryParam.getPage_size(), TradeLine.class, term.toArray());
		return page;
	}

}
