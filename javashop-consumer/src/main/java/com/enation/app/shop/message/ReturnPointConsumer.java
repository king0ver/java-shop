package com.enation.app.shop.message;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.core.event.IRefundStatusChangeEvent;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
import com.enation.app.shop.member.service.IMemberPointManger;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.framework.database.IDaoSupport;

/**
 * 
 * 积分商品的退货/退款要把积分退还给用户
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年11月17日 上午11:09:08
 */
@Component
public class ReturnPointConsumer implements IRefundStatusChangeEvent {
	@Autowired
	private IOrderQueryManager orderQueryManager;
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IMemberPointManger memberPointManger;

	@Override
	public void refund(RefundChangeMessage refundPartVo) {
		if (refundPartVo.getOperation_type().equals(RefundChangeMessage.ADMIN_AUTH)) {
			OrderDetail orderDetail = this.orderQueryManager.getOneBySn(refundPartVo.getRefund().getOrder_sn());
			String sql = "select * from es_member where member_id= ? ";
			Member member = daoSupport.queryForObject(sql, Member.class, orderDetail.getMember_id());
			// 积分
			Integer point = refundPartVo.getRefund().getRefund_point();
			if (point != null && point > 0) {
				memberPointManger.add(member, 0, "退款/退货，归还消费积分", null, point, 1);
			}

		}

	}

}
