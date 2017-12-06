package com.enation.app.shop.order.consumer;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.promotion.bonus.model.BonusType;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.vo.Coupon;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 确认收款发放促销活动赠送优惠券
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月26日 下午8:30:02
 */
@Component
public class OrderPaySendBonusConsumer implements IOrderStatusChangeEvent {
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		if ((orderMessage.getNewStatus().name()).equals(OrderStatus.PAID_OFF.name())) {
			Gson gson = new Gson();
			String sql = "select meta_value from es_order_meta where order_sn=? and meta_key='coupon'";
			String coupon = daoSupport.queryForString(sql, orderMessage.getOrder().getSn());
			List<Coupon> couponlist = gson.fromJson(coupon, new TypeToken<List<Coupon>>() {
			}.getType());
			if (couponlist != null && couponlist.size() > 0) {
				// 循环发放的优惠券
				for (Coupon coupons : couponlist) {
					long currTime = DateUtil.getDateline();
					// 获取当前发放优惠券的有所信息
					String couponsql = "select * from es_bonus_type where type_id=?";
					BonusType bonusType = daoSupport.queryForObject(couponsql, BonusType.class, coupons.getCoupon_id());
					String sn = this.createSn(bonusType.getType_id() + "");
					int c = this.daoSupport.queryForInt("select count(0) from es_member_bonus where bonus_sn=?", sn);
					if (c == 0) {
						// 将当前发放优惠券的信息塞入到会员优惠券表中
						this.daoSupport.execute(
								"insert into es_member_bonus(bonus_type_id,bonus_sn,type_name,bonus_type,create_time,member_id)values(?,?,?,?,?,?)",
								bonusType.getType_id(), sn, bonusType.getType_name(), bonusType.getSend_type(),
								currTime, orderMessage.getOrder().getMember_id());

						// 修改优惠券已被领取的数量
						this.daoSupport.execute(
								"update es_bonus_type set received_num = (received_num + 1) where type_id = ?",
								bonusType.getType_id());
					} else {
						System.out.println("有相同的sn码,在生成一个sn码");
					}

				}
			}
		}

	}

	/**
	 * 生成实体券编码
	 * 
	 * @param prefix
	 * @return
	 */
	private String createSn(String prefix) {

		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append(DateUtil.toString(new Date(), "yyMM"));
		sb.append(createRandom());

		return sb.toString();
	}

	/**
	 * 创建随机数
	 * 
	 * @return
	 */
	private String createRandom() {
		Random random = new Random();
		StringBuffer pwd = new StringBuffer();
		for (int i = 0; i < 6; i++) {
			pwd.append(random.nextInt(9));

		}
		return pwd.toString();
	}
}
