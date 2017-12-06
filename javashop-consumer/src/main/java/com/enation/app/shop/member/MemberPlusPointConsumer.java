package com.enation.app.shop.member;

import com.enation.app.base.core.model.Member;
import com.enation.app.core.event.IGoodsCommentEvent;
import com.enation.app.core.event.IMemberInfoCompleteEvent;
import com.enation.app.core.event.IMemberLoginEvent;
import com.enation.app.core.event.IMemberRegisterEvent;
import com.enation.app.core.event.IOrderStatusChangeEvent;
import com.enation.app.shop.comments.model.vo.GoodsCommentMsg;
import com.enation.app.shop.comments.service.IMemberCommentManager;
import com.enation.app.shop.member.model.vo.MemberLoginMsg;
import com.enation.app.shop.member.model.vo.MemberRegistVo;
import com.enation.app.shop.member.service.IMemberManager;
import com.enation.app.shop.member.service.IMemberPointManger;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.support.OrderStatusChangeMessage;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * 确认收货会员增加促销积分
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月26日 下午6:24:01
 */
@Component
public class MemberPlusPointConsumer implements IOrderStatusChangeEvent ,IMemberLoginEvent,IMemberRegisterEvent,IGoodsCommentEvent,IMemberInfoCompleteEvent{
	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private IMemberPointManger memberPointManger;
	@Autowired
	private IMemberManager memberManager;
	@Autowired
	private IMemberCommentManager memberCommentManager;

	@Override
	public void orderChange(OrderStatusChangeMessage orderMessage) {
		// 确认收货会员增加促销积分
		if (orderMessage.getNewStatus().name().equals(OrderStatus.ROG.name())) {
			
			String sql = "select * from es_member where member_id= ? ";
			Member member = daoSupport.queryForObject(sql, Member.class, orderMessage.getOrder().getMember_id());
			if (memberPointManger.checkIsOpen(IMemberPointManger.TYPE_BUYGOODS)) {
				int point = memberPointManger.getItemPoint(IMemberPointManger.TYPE_BUYGOODS + "_num");
				int mp = memberPointManger.getItemPoint(IMemberPointManger.TYPE_BUYGOODS + "_num_mp");
				// 确认收货后按找后台设置的比例乘商品价格
				memberPointManger.add(member,CurrencyUtil.mul(point, orderMessage.getOrder().getGoods_price()).intValue(), "购买商品", null, 0,0);
				memberPointManger.add(member, 0, "购买商品", null,CurrencyUtil.mul(mp, orderMessage.getOrder().getGoods_price()).intValue(), 1);
			}
			OrderPo order = orderMessage.getOrder();
			if(order.getPayment_type().equals(PaymentType.online.name())) {
				this.set(IMemberPointManger.TYPE_ONLINEPAY, "在线支付", orderMessage.getOrder().getMember_id());
			}
			//增加赠送积分
			sql = "select meta_value from es_order_meta where order_sn=? and meta_key='giftPoint'";
			Integer giftPoint = daoSupport.queryForInt(sql,orderMessage.getOrder().getSn());
			if (giftPoint!=null&&!giftPoint.equals(0)){
				memberPointManger.add(member, 0, "满赠", null,giftPoint, 1);
			}
		}
	}

	@Override
	public void memberLogin(MemberLoginMsg memberLoginMsg) {
		if (memberPointManger.checkIsOpen(IMemberPointManger.TYPE_LOGIN)) {
			long ldate = ((long)memberLoginMsg.getLast_login_time())*1000;
			Date date = new Date(ldate);
			Date today = new Date();
			if(!DateUtil.toString(date, "yyyy-MM-dd").equals(DateUtil.toString(today, "yyyy-MM-dd"))){//非今天
				this.set(IMemberPointManger.TYPE_LOGIN, DateUtil.toString(today, "yyyy年MM月dd日")+"登录", memberLoginMsg.getMember_id());
			}
		}
	}

	@Override
	public void memberRegister(MemberRegistVo vo) {
		this.set(IMemberPointManger.TYPE_REGISTER, "成功注册", vo.getMember_id());
	}

	@Override
	public void goodsComment(GoodsCommentMsg goodsCommentMsg) {
		List commentGallery = memberCommentManager.getCommentGallery(goodsCommentMsg.getComment_id());
		if(memberPointManger.checkIsOpen(IMemberPointManger.TYPE_COMMENT_IMG)&&commentGallery!=null&&commentGallery.size()>0) {
			this.set(IMemberPointManger.TYPE_COMMENT_IMG, "上传图片评论", goodsCommentMsg.getMember_id());
		} else if(memberPointManger.checkIsOpen(IMemberPointManger.TYPE_COMMENT)) {
			this.set(IMemberPointManger.TYPE_COMMENT, "文字评论", goodsCommentMsg.getMember_id());
		}
		
		// 首次评论额外50积分
		if (memberCommentManager.getGoodsCommentsCount(goodsCommentMsg.getGoods_id()) == 0) {
			this.set(IMemberPointManger.TYPE_FIRST_COMMENT, "首次评论", goodsCommentMsg.getMember_id());
		}
	}
	
	@Override
	public void memberInfoComplete(Integer member_id) {
		this.set(IMemberPointManger.TYPE_FINISH_PROFILE, "完善个人资料", member_id);
		
	}
	
	private void set(String type,String reson,Integer member_id) {
		Member member = memberManager.get(member_id);
		if (memberPointManger.checkIsOpen(type)) {
			int point = memberPointManger.getItemPoint(type+ "_num");
			int mp = memberPointManger.getItemPoint(type+ "_num_mp");
			memberPointManger.add(member, point,reson, null, 0,0);
			memberPointManger.add(member, 0,reson, null, mp,1);
		}
	}

	
}
