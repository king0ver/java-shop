package com.enation.app.shop.trade.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.enums.CommentStatus;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.enums.PayStatus;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.enums.ShipStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.OperateAllowable;
import com.enation.app.shop.trade.model.vo.Order;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.OrderLineSeller;
import com.enation.app.shop.trade.model.vo.OrderQueryParam;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ResourceNotFoundException;

/**
 * 订单数据库查询业务类
 *
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月28日上午10:00:46
 */
@Service("orderQueryManager")
public class OrderDBQueryManager implements IOrderQueryManager {

    @Autowired
    private IDaoSupport daoSupport;



    @Override
    public OrderDetail getOneBySn(String ordersn) {
        String sql = "select * from es_order where sn=?";
        OrderDetail detail = this.daoSupport.queryForObject(sql, OrderDetail.class, ordersn);

        if (detail == null) {
            throw new ResourceNotFoundException("", "订单[" + ordersn + "]不存在");
        }
        // 初始化订单允许状态
        OperateAllowable operateAllowable = new OperateAllowable(PaymentType.valueOf(detail.getPayment_type()),
                OrderStatus.valueOf(detail.getOrder_status()), CommentStatus.valueOf(detail.getComment_status()),
                ShipStatus.valueOf(detail.getShip_status()),ServiceStatus.valueOf(detail.getService_status()),
                PayStatus.valueOf(detail.getPay_status()));
        detail.setOperateAllowable(operateAllowable);

        return detail;
    }


    @Override
    public List<Order> queryByTradeSnGetOrder(String tradeSn) {
        String tradeSql = new String("select * from es_order where trade_sn=?");
        List<Order> order_json = daoSupport.queryForList(tradeSql, Order.class, tradeSn);
        return order_json;
    }

    /**
     * 查询卖家订单
     */
    @Override
    public Page<OrderLineSeller> query(OrderQueryParam queryParam) {

        // TODO 此处不能用*
        StringBuffer sql = new StringBuffer("select * from es_order o where disabled=0 ");
        List<Object> term = new ArrayList<Object>();

        // 按卖家查询
        Integer sellerid = queryParam.getSeller_id();
        if (sellerid != null) {
            sql.append(" and o.seller_id=?");
            term.add(sellerid);
        }

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
            if (tag.equals("wait_ship")) {
                sql.append(" and( ( payment_type!='cod'  and  order_status='" + OrderStatus.PAID_OFF + "')  ");// 非货到付款的，要已结算才能发货
                sql.append(" or ( payment_type='cod' and  order_status='" + OrderStatus.CONFIRM + "')) ");// 货到付款的，已确认就可以发货
                // 待付款
            } else if (tag.equals("wait_pay")) {
                sql.append(" and ( ( payment_type!='cod' and  order_status='" + OrderStatus.CONFIRM + "') ");// 非货到付款的，未付款状态的可以结算
                sql.append(" or ( payment_type='cod' and   order_status='" + OrderStatus.ROG + "'  ) )");// 货到付款的要发货或收货后才能结算
                // 待收货
            } else if (tag.equals("wait_rog")) {
                sql.append(" and o.order_status='" + OrderStatus.SHIPPED + "'");
            }else if (tag.equals("cancel")) {
                sql.append(" and o.order_status='" + OrderStatus.CANCELLED + "'");
            }else if (tag.equals("complete")) {
                sql.append(" and o.order_status='" + OrderStatus.COMPLETE + "'");
            }else if(tag.equals("wait_comment")) {
        			sql.append(" and ( o.ship_status='" + ShipStatus.SHIP_ROG + "' and o.comment_status='"+CommentStatus.UNFINISHED+"') ");
            }
        }
        //订单状态
        if(!StringUtil.isEmpty(queryParam.getOrder_status())){
            sql.append(" and o.order_status = ?");
            term.add(queryParam.getOrder_status());
        }
        

        if (StringUtil.notEmpty(queryParam.getBuyer_name())) {
            sql.append(" and o.items_json like ?");
            term.add("%" + queryParam.getGoods_name() + "%");
        }
        if (StringUtil.notEmpty(queryParam.getShip_name())) {
            sql.append(" and o.ship_name like ?");
            term.add("%" + queryParam.getShip_name() + "%");
        }
        // 按商品名称查询
        if (StringUtil.notEmpty(queryParam.getGoods_name())) {
            sql.append(" and o.items_json like ?");
            term.add("%" + queryParam.getGoods_name() + "%");
        }
        // if(StringUtil.notEmpty( queryParam.getOrder_sn() )){
        sql.append(" order by o.create_time desc");
        // 先按PO进行查询
        Page<OrderPo> page = daoSupport.queryForPage(sql.toString(), queryParam.getPage_no(), queryParam.getPage_size(),
                OrderPo.class, term.toArray());

        // 转为VO
        List<OrderPo> orderList = (List)page.getResult();
        List<OrderLineSeller> lineList = new ArrayList();
        for (OrderPo orderPo : orderList) {
            OrderLineSeller line = new OrderLineSeller(orderPo);
            lineList.add(line);
        }

        // 生成新的Page
        long totalCount = page.getTotalCount();
        Page linePage = new Page(queryParam.getPage_no(), totalCount, queryParam.getPage_size(), lineList);

        return linePage;
    }

    @Override
    public Map<Object, Integer> getOrderNum(OrderQueryParam queryParam) {
        StringBuffer sql = new StringBuffer("select order_status,count(*) as count from es_order o where 1=1");
        List<Object> term = new ArrayList<Object>();
        // 按卖家查询
        Integer sellerid = queryParam.getSeller_id();
        if (sellerid != null) {
            sql.append(" and o.seller_id=?");
            term.add(sellerid);
        }

        // 按买家查询
        Integer memberid = queryParam.getMember_id();
        if (memberid != null) {
            sql.append(" and o.member_id=?");
            term.add(memberid);
        }
        sql.append(" GROUP BY order_status");
        Map<Object,Integer> map = new HashMap<>();
        List<Map<String,Integer>> list = this.daoSupport.queryForList(sql.toString(),term.toArray());
        Integer total = 0;
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).get("order_status"),StringUtil.toInt(list.get(i).get("count"),false));
            total += StringUtil.toInt(list.get(i).get("count"),false);
        }
        map.put("TOTAL",total);
        return map;
    }


}
