package com.enation.app.shop.trade.plugin;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.trade.model.enums.*;
import com.enation.app.shop.trade.service.ITradeSnCreator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.operator.Cancel;
import com.enation.app.shop.trade.model.vo.operator.Complete;
import com.enation.app.shop.trade.model.vo.operator.Rog;
import com.enation.app.shop.trade.plugin.setting.OrderSetting;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.eop.SystemSetting;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;


/**
 * 订单状态定时任务
 *
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月8日 下午2:01:09
 */
@Component
@AutoRegister
public class OrderStatusCheckPlugin extends AutoRegisterPlugin implements IEveryDayExecuteEvent {

    @Autowired
    private ITradeSnCreator tradeSnCreator;

    @Autowired
    private IDaoSupport daoSupport;

    @Autowired
    private IOrderOperateManager orderOperateManager;

    protected final Logger logger = Logger.getLogger(getClass());

    @Override
    public void everyDay() {

        try {
            //清除昨天的sn生成的缓存key
            tradeSnCreator.cleanCache();
        } catch (Exception e) {
            logger.error("清除昨天的sn生成的缓存key出错", e);
        }

        try {
            OrderSetting.load();
        } catch (Exception e) {
            logger.error("订单配置加载出错", e);
        }


        try {
            //款到发货型，新订单24小时未付款要自动取消
            this.checkCancel();
        } catch (Exception e) {
            logger.error("自动取消出错", e);
        }
        try {
            //发货之后15天要自动确认收货
            this.checkRog();
        } catch (Exception e) {
            logger.error("动确认收货出错", e);
        }
        try {
            //确认收货7天后标记为完成
            this.checkcmpl();
        } catch (Exception e) {
            logger.error("订单7天后标记为完成出错", e);
        }
        try {
            //完成后一个月没有申请售后，标记为售后过期
            this.checkAfterService();
        } catch (Exception e) {
            logger.error("订单标记为售后过期出错", e);
        }
        try {
            //超过14天不能评价
            this.checkComment();
        } catch (Exception e) {
            logger.error("订单超过14天不能评价出错", e);
        }
    }

    /**
     * 超过14天自动好评
     */
    private void checkComment() {
        //TODO 超过多少钱自动评价
        Integer time = OrderSetting.comment_order_day * 24 * 60 * 60;
        if (SystemSetting.getTest_mode() == 1) {//如果测试模式开启 将时间改为60秒
            time = 60;
        }
//		String sql="SELECT tr.member_id,tr.goods_id from es_member_order_item mo INNER JOIN es_transaction_record tr ON mo.order_id=tr.order_id WHERE mo.commented=0  and tr.rog_time+?<? GROUP BY tr.member_id,tr.goods_id ";
//		List<Map> list= daoSupport.queryForList(sql,time,DateUtil.getDateline());
//		
//		MemberComment memberComment = new MemberComment();

//		for(Map map :list){
//			Integer goods_id=Integer.parseInt(map.get("goods_id").toString()) ;
//			Integer member_id= Integer.parseInt(map.get("member_id").toString());
//			memberComment.setGoods_id(goods_id);
//			if("b2c".equals(EopSetting.PRODUCT)){
//				memberComment.setGrade(3);
//			}else{
//				memberComment.setGrade(5);
//			}
//			memberComment.setImg(null);
//			memberComment.setMember_id(member_id );
//			memberComment.setDateline(DateUtil.getDateline());
//			memberComment.setType(1);
//			memberComment.setContent("东西不错！我很喜欢！店主耐心！");
//			memberComment.setStatus(1);
//			memberCommentManager.add(memberComment);
//			//更新为已经评论过此商品
//			MemberOrderItem memberOrderItem = memberOrderItemManager.get(member_id,goods_id,0);
//			if(memberOrderItem != null){
//				memberOrderItem.setCommented(1);
//				memberOrderItem.setComment_time(DateUtil.getDateline());
//				memberOrderItemManager.update(memberOrderItem);
//			}
//		}
    }

    /**
     * 检测完成后一个月没有申请售后，标记为售后过期
     */
    private void checkAfterService() {

        long unix_timestamp = DateUtil.getDateline();
        Integer time = OrderSetting.service_expired_day * 24 * 60 * 60;
        if (SystemSetting.getTest_mode() == 1) {//如果测试模式开启 将时间改为60秒
            time = 60;
        }
        String sql = "select sn from es_order where complete_time+?<? and order_status=? and payment_type!=? and service_status!=?";
        //查询所有非货到付款并且订单状态为已收货的订单
        List<Map> list = this.daoSupport.queryForList(sql, time, unix_timestamp, OrderStatus.COMPLETE.value(), PaymentType.cod.value(),ServiceStatus.EXPIRED.value());

        for (Map map : list) {
            this.orderOperateManager.updateServiceStatus(map.get("sn").toString(), ServiceStatus.EXPIRED);
        }

    }

    /**
     * 检测订单的完成状态
     * 确认收货7天后标记为完成
     */
    private void checkcmpl() {

        long unix_timestamp = DateUtil.getDateline();
        Integer time = OrderSetting.complete_order_day * 24 * 60 * 60;
        if (SystemSetting.getTest_mode() == 1) {//如果测试模式开启 将时间改为60秒
            time = 60;
        }
        String sql = "select sn from es_order where signing_time+?<? and  ship_status=? and payment_type!=? and order_status!=?";
        //查询所有非货到付款并且订单状态为已收货的订单
        List<Map> list = this.daoSupport.queryForList(sql, time, unix_timestamp, ShipStatus.SHIP_ROG.value(), PaymentType.cod.value(),OrderStatus.COMPLETE.value());

        //货到付款的，确认收款之后n天为完成
        sql = "select sn from es_order where signing_time+?<? and  pay_status=? and payment_type=? and order_status!=?";
        //查询所有非货到付款并且订单状态为已收货的订单
        List<Map>  list2 = this.daoSupport.queryForList(sql, time, unix_timestamp, PayStatus.PAY_YES.value(),PaymentType.cod.value(),OrderStatus.COMPLETE.value());
        list.addAll(list2);

        for (Map map : list) {
            Complete complete = new Complete(map.get("sn").toString(), "系统检测");
            this.orderOperateManager.complete(complete, OrderPermission.client);
        }
    }

    /**
     * 发货之后15天要自动确认收货
     */
    private void checkRog() {
        //查询已发货 的订单

        long unix_timestamp = DateUtil.getDateline();
        Integer time = OrderSetting.rog_order_day * 24 * 60 * 60;
        if (SystemSetting.getTest_mode() == 1) {//如果测试模式开启 将时间改为60秒
            time = 60;
        }
        String sql = "select sn from es_order where order_status = ? and payment_type != ? and ship_time+?<? ";

        List<Map> list = this.daoSupport.queryForList(sql, OrderStatus.SHIPPED, PaymentType.cod.value(), time, DateUtil.getDateline());
        for (Map map : list) {
            Rog rog = new Rog(map.get("sn").toString(), "系统检测");
            this.orderOperateManager.rog(rog, OrderPermission.client);
        }

    }

    /**
     * 检测订单取消
     * 订单超时未操作则取消订单
     * 新订单24小时未付款要自动取消
     */
    private void checkCancel() {
        Integer time = OrderSetting.cancel_order_day * 24 * 60 * 60;
        if (SystemSetting.getTest_mode() == 1) {//如果测试模式开启 将时间改为60秒
            time = 60;
        }
        String sql = "SELECT sn from es_order  WHERE payment_type!=? AND create_time+?<? AND (order_status=? or order_status=? )";
        List<Map> list = daoSupport.queryForList(sql, PaymentType.cod.value(), time, DateUtil.getDateline(), OrderStatus.NEW.value(), OrderStatus.CONFIRM.value());

        for (Map map : list) {
            Cancel cancel = new Cancel(map.get("sn").toString(), "超时未付款", "系统检测");
            this.orderOperateManager.cancel(cancel, OrderPermission.client);
        }
    }

}
