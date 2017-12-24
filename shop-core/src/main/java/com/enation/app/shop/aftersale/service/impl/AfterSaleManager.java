package com.enation.app.shop.aftersale.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.AmqpExchange;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.aftersale.model.enums.RefundOperate;
import com.enation.app.shop.aftersale.model.enums.RefundStatus;
import com.enation.app.shop.aftersale.model.enums.RefuseType;
import com.enation.app.shop.aftersale.model.po.Refund;
import com.enation.app.shop.aftersale.model.po.RefundGoods;
import com.enation.app.shop.aftersale.model.po.RefundLog;
import com.enation.app.shop.aftersale.model.vo.ApplyReturnGoods;
import com.enation.app.shop.aftersale.model.vo.BuyerRefundApply;
import com.enation.app.shop.aftersale.model.vo.FinanceRefundApproval;
import com.enation.app.shop.aftersale.model.vo.RefundDetail;
import com.enation.app.shop.aftersale.model.vo.RefundGoodsVo;
import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.aftersale.model.vo.RefundQueryParam;
import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.aftersale.model.vo.SellerRefundApproval;
import com.enation.app.shop.aftersale.model.vo.StockIn;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.app.shop.aftersale.service.RefundOperateChecker;
import com.enation.app.shop.aftersale.support.RefundChangeMessage;
import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.app.shop.payment.service.IOrderPayManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.enums.ServiceStatus;
import com.enation.app.shop.trade.model.po.OrderMeta;
import com.enation.app.shop.trade.model.vo.OrderDetail;
import com.enation.app.shop.trade.model.vo.OrderSkuVo;
import com.enation.app.shop.trade.model.vo.Product;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;
import com.enation.framework.validator.ResourceNotFoundException;
import com.enation.framework.validator.UnProccessableServiceException;
import com.google.gson.Gson;

/**
 * 售后管理业务类
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月24日下午9:26:05
 */
@Service
public class AfterSaleManager implements IAfterSaleManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IOrderQueryManager orderQueryManager;

	@Autowired
	private IOrderOperateManager orderOperateManager;

	@Autowired
	private ISellerManager sellerManager;

	@Autowired
	private IOrderPayManager orderPayManager;
	@Autowired
	private IGoodsQuantityManager goodsQuantityManager;
	@Autowired
	private AmqpTemplate amqpTemplate;

	@Autowired
	private ICache cache;

	@Transactional(propagation = Propagation.REQUIRED)
	private Refund innerRefund(BuyerRefundApply buyerRefundApply) {
		// 操作是否允许
		boolean operateAllowable = false;
		String ordersn = buyerRefundApply.getOrder_sn();
		OrderDetail order = this.orderQueryManager.getOneBySn(ordersn);
		if (order == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "订单找不到");
		}

		Member member = UserConext.getCurrentMember();
		if (member == null || member.getMember_id() == null
				|| member.getMember_id().intValue() != order.getMember_id().intValue()) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权操作此订单");
		}
		List<Product> productList = order.getProductList();
		String refund_sn = DateUtil.toString(DateUtil.getDateline(), "yyMMddhhmmss");
		Gson gson = new Gson();

		Product product = null;
		for (Product goods : productList) {
			if (goods.getProduct_id() == (buyerRefundApply.getSku_id()).intValue()) {
				product = goods;
				goods.setService_status(ServiceStatus.APPLY.value());
				OrderSkuVo orderSkuVo = new OrderSkuVo(order.getOrder_status(), order.getPay_status(),
						order.getShip_status(), order.getPayment_type());
				operateAllowable = orderSkuVo.isAllowApplyService();
				break;
			}
		}
		String item_json = gson.toJson(productList);
		this.orderOperateManager.updateItemJson(item_json, order.getSn());
		// 是否允许申请售后
		if (!operateAllowable) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "操作不允许");
		}

		if (product == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "该商品不存在");
		}

		if (buyerRefundApply.getRefund_price() > product.getSubtotal()) {
			throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "退款金额不能大于商品支付金额");
		}
		// 商品入库
		RefundGoods refundGoods = new RefundGoods(product);
		refundGoods.setReturn_num(buyerRefundApply.getReturn_num());
		refundGoods.setPrice(product.getSubtotal());// 实际支付的价格
		refundGoods.setRefund_sn(refund_sn);

		// 退货时判断，如果第一次退货把赠品塞到refund_gift，否则塞空
		if (buyerRefundApply.getRefuseType().equals(RefuseType.return_goods.value())) {
			String sql = "select * from es_order_meta where order_sn=? and meta_key='gift' and status=?";
			OrderMeta refundGift = this.daoSupport.queryForObject(sql, OrderMeta.class, ordersn,
					ServiceStatus.NOT_APPLY.name());
			// 如果没有申请过退货，把赠品信息塞到es_refund_goods表
			if (refundGift != null) {
				refundGoods.setRefund_gift(refundGift.getMeta_value());
				refundGift.setStatus(ServiceStatus.APPLY.name());
				// 修改赠品的状态改为“已申请”
				this.daoSupport.update("es_order_meta", refundGift, "order_sn=" + ordersn);
			}
		}
		this.daoSupport.insert("es_refund_goods", refundGoods);

		// 退款单入库
		Refund refund = new Refund();
		refund.setSn(refund_sn);
		refund.setOrder_sn(buyerRefundApply.getOrder_sn());
		refund.setAccount_type(buyerRefundApply.getAccount_type());
		refund.setCustomer_remark(buyerRefundApply.getCustomer_remark());
		refund.setRefund_price(buyerRefundApply.getRefund_price());
		refund.setRefund_reason(buyerRefundApply.getRefund_reason());
		refund.setRefund_way(buyerRefundApply.getRefund_way());
		if (buyerRefundApply.getRefund_point() == null) {
			buyerRefundApply.setRefund_point(0);
		}
		refund.setRefund_point(buyerRefundApply.getRefund_point());
		if (!"original".equals(refund.getRefund_way())) {
			if ("银行转账".equals(refund.getAccount_type())) {
				refund.setBank_account_name(buyerRefundApply.getBank_account_name());
				refund.setBank_account_number(buyerRefundApply.getBank_account_number());
				refund.setBank_deposit_name(buyerRefundApply.getBank_deposit_name());
				refund.setBank_name(buyerRefundApply.getBank_name());
			} else {
				refund.setReturn_account(buyerRefundApply.getReturn_account());
			}
		}
		refund.setMember_id(member.getMember_id());
		refund.setMember_name(member.getUname());
		refund.setSeller_id(order.getSeller_id());
		refund.setSeller_name(order.getSeller_name());
		refund.setTrade_sn(order.getTrade_sn());
		refund.setPay_order_no(order.getPay_order_no());
		refund.setCreate_time(DateUtil.getDateline());
		refund.setRefund_status(RefundStatus.apply.value());
		refund.setRefuse_type(buyerRefundApply.getRefuseType());
		refund.setRefund_point(buyerRefundApply.getRefund_point());
		this.daoSupport.insert("es_refund", refund);
		refund.setId(this.daoSupport.getLastId("es_refund"));

		// 发送申请退款的消息
		RefundChangeMessage refundStatusChangeMessage = new RefundChangeMessage(refund, RefundChangeMessage.APPLY);
		amqpTemplate.convertAndSend(AmqpExchange.REFUND_STATUS_CHANGE.name(), "refund-apply-routingKey",
				refundStatusChangeMessage);

		return refund;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyRefund(BuyerRefundApply refundApply) {
		Refund refund = this.innerRefund(refundApply);
		this.log(refund.getSn(), refund.getMember_name(), "申请退款");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyGoodsReturn(BuyerRefundApply goodsReturnsApply) {
		Refund refund = this.innerRefund(goodsReturnsApply);
		this.log(refund.getSn(), refund.getMember_name(), "申请退货");
	}

	@Override
	public Page<RefundVo> query(RefundQueryParam param) {

		StringBuffer sql = new StringBuffer();

		List termList = new ArrayList();

		sql.append("select * from es_refund where 1=1 ");
		/** 需要查询退款且审核通过的 退货且全部入库的数据 */
		if (param.getIdent() == 1) {
			sql.append(" AND (refund_status = ? ");
			termList.add(RefundStatus.pass.value());
			sql.append(" AND refuse_type = ? )");
			termList.add(RefuseType.return_money.value());
			sql.append(" OR (refund_status = ? ");
			termList.add(RefundStatus.all_stock_in.value());
			sql.append(" AND refuse_type = ?)");
			termList.add(RefuseType.return_goods.value());
		}

		String sn = param.getSn();
		if (StringUtil.notEmpty(sn)) {
			sql.append(" and sn=?");
			termList.add(sn);
		}

		String refund_status = param.getRefund_status();
		if (StringUtil.notEmpty(refund_status)) {
			sql.append(" and refund_status=?");
			termList.add(refund_status);
		}

		Integer sellerid = param.getSeller_id();
		if (sellerid != null) {
			sql.append(" and seller_id=?");
			termList.add(sellerid);
		}

		String seller_name = param.getSeller_name();
		if (StringUtil.notEmpty(seller_name)) {
			sql.append(" and seller_name like ?");
			termList.add("%" + seller_name + "%");
		}

		Integer memberid = param.getMember_id();
		if (memberid != null) {
			sql.append(" and member_id=?");
			termList.add(memberid);
		}

		String order_sn = param.getOrder_sn();
		if (StringUtil.notEmpty(order_sn)) {
			sql.append(" and order_sn=?");
			termList.add(order_sn);
		}

		String refuse_type = param.getRefuse_type();
		if (StringUtil.notEmpty(refuse_type)) {
			sql.append(" and refuse_type=?");
			termList.add(refuse_type);
		}

		// 按时间查询
		String start_time = param.getStart_time();
		String end_time = param.getEnd_time();
		if (StringUtil.notEmpty(start_time)) {
			sql.append(" and create_time >= ?");
			termList.add(DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		}

		if (StringUtil.notEmpty(end_time)) {
			sql.append(" and create_time <= ?");
			termList.add(DateUtil.getDateline(end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss"));

		}

		sql.append(" order by create_time desc");
		Page page = this.daoSupport.queryForPage(sql.toString(), param.getPage_no(), param.getPage_size(),
				RefundVo.class, termList.toArray());

		return page;
	}

	@Override
	public RefundDetail getDetail(String sn) {

		RefundVo refundLine = this.daoSupport.queryForObject("select * from es_refund where sn =?", RefundVo.class, sn);

		if (refundLine == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "退货（款）单[" + sn + "]找不到");
		}

		RefundGoodsVo goods = this.daoSupport.queryForObject("select * from es_refund_goods where refund_sn=? ",
				RefundGoodsVo.class, sn);
		RefundDetail refundDetail = new RefundDetail();
		refundDetail.setRefund(refundLine);
		refundDetail.setGoods(goods);

		return refundDetail;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public SellerRefundApproval approval(SellerRefundApproval refundApproval) {

		Seller seller = sellerManager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "未登录没有权限");
		}

		RefundVo refund = this.daoSupport.queryForObject("select * from es_refund where sn =?", RefundVo.class,
				refundApproval.getSn());
		if (refund == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,
					"退货（款）单[" + refundApproval.getSn() + "]找不到");
		}

		this.checkAllowable(refund, RefundOperate.seller_approval);

		// 如果已经审核过，则直接跳过
		if (refund.getRefund_status().equals(RefundStatus.pass.value())
				|| refund.getRefund_status().equals(RefundStatus.refuse.value())) {

			return refundApproval;
		}

		// 验证是否当前卖家的退款单
		Integer seller_id = refund.getSeller_id();

		if (seller_id.compareTo(seller.getStore_id()) != 0) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}

		// 判断是否同意退款
		String refund_status = RefundStatus.refuse.value();
		if (refundApproval.isAgree()) {
			refund_status = RefundStatus.pass.value();
			refund.setRefund_status(refund_status);
			refund.setRefund_price(refundApproval.getRefund_price());
			// 发送审核通过的消息
			RefundChangeMessage refundStatusChangeMessage = new RefundChangeMessage(refund, RefundChangeMessage.AUTH);
			amqpTemplate.convertAndSend(AmqpExchange.REFUND_STATUS_CHANGE.name(), "refund-apply-routingKey",
					refundStatusChangeMessage);
		}
		// 更新状态
		if (refundApproval.getRefund_point() == null) {
			refundApproval.setRefund_point(0);
		}
		this.daoSupport.execute(
				"update es_refund set refund_status=?,seller_remark=? ,refund_price=? ,refund_point=? where sn =?",
				refund_status, refundApproval.getRemark(), refundApproval.getRefund_price(),
				refundApproval.getRefund_point(), refundApproval.getSn());

		// 记录日志
		this.log(refundApproval.getSn(), seller.getName(), "审核退货（款），结果为：" + (refundApproval.isAgree() ? "同意" : "拒绝"));

		return refundApproval;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public FinanceRefundApproval approval(FinanceRefundApproval refundApproval) {
		if (refundApproval.getRefund_point() == null) {
			refundApproval.setRefund_point(0);
		}

		AdminUser adminUser = UserConext.getCurrentAdminUser();
		if (adminUser == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}

		RefundVo refund = this.daoSupport.queryForObject("select * from es_refund where sn =?", RefundVo.class,
				refundApproval.getSn());
		if (refund == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,
					"退货（款）单[" + refundApproval.getSn() + "]找不到");
		}

		this.checkAllowable(refund, RefundOperate.admin_approval);

		// 查看退款是原路退回，还是线下打款
		String refundWay = refund.getRefund_way();// 退款方式
		// amqpSwitch为true 说明流程正确，需要发送消息
		boolean amqpSwitch;
		if (refundWay.equals("original") && !refund.getRefund_status().equals(RefundStatus.refundfail)) {// 原路退回
			if (refundApproval.getRefund_price() == 0) {
				amqpSwitch = false;
				this.daoSupport.execute("update es_refund set refund_point=? ,refund_price=? where sn=?",
						refundApproval.getRefund_point(), refundApproval.getRefund_price(), refundApproval.getSn());
			}
			// 订单号 退款金额
			boolean isSuccess = orderPayManager.returnOrder(refund);
			if (isSuccess) {
				this.daoSupport.execute("update es_refund set refund_status=? where sn =?",
						RefundStatus.refunding.value(), refundApproval.getSn());
				refundApproval.setIs_success(true);
				amqpSwitch = true;
			} else {
				String error = (String) this.cache.get("REFUND_ERROR_MESSAGE");
				refundApproval.setError_code(error);
				refundApproval.setIs_success(false);
				this.cache.remove("REFUND_ERROR_MESSAGE");
				amqpSwitch = false;
			}
		} else {
			refundApproval.setIs_success(true);
			this.daoSupport.execute("update es_refund set refund_status=? where sn =?", RefundStatus.completed.value(),
					refundApproval.getSn());
			amqpSwitch = true;

		}
		if (amqpSwitch) {
			this.daoSupport.execute("update es_refund set refund_point=? ,refund_price=? where sn=?",
					refundApproval.getRefund_point(), refundApproval.getRefund_price(), refundApproval.getSn());
			// 发送管理员审核的消息
			RefundChangeMessage refundStatusChangeMessage = new RefundChangeMessage(refund,
					RefundChangeMessage.ADMIN_AUTH);
			amqpTemplate.convertAndSend(AmqpExchange.REFUND_STATUS_CHANGE.name(), "refund-apply-routingKey",
					refundStatusChangeMessage);
		}
		return refundApproval;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public StockIn SellerStockIn(StockIn stockIn) {
		Seller seller = sellerManager.getSeller();
		if (seller == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "未登录没有权限");
		}
		RefundVo refund = this.daoSupport.queryForObject("select * from es_refund where sn =?", RefundVo.class,
				stockIn.getSn());
		if (refund == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "退货（款）单[" + stockIn.getSn() + "]找不到");
		}
		this.checkAllowable(refund, RefundOperate.stock_in);
		// 如果已经入库
		if (refund.getRefund_status().equals(RefundStatus.all_stock_in.value())) {
			return stockIn;
		}
		// 验证是否当前卖家的退款单
		Integer seller_id = refund.getSeller_id();
		if (seller_id.compareTo(seller.getStore_id()) != 0) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问");
		}
		this.daoSupport.execute("update es_refund set refund_status=? where sn =?", RefundStatus.all_stock_in.value(),
				stockIn.getSn());

		ApplyReturnGoods goods = this.daoSupport.queryForObject(
				"select goods_id,sku_id,return_num from es_refund_goods where refund_sn =?", ApplyReturnGoods.class,
				stockIn.getSn());

		// 商品入库
		GoodsQuantityVo goodsQuantity = new GoodsQuantityVo();
		goodsQuantity.setSku_id(goods.getSku_id());
		goodsQuantity.setGoods_id(goods.getGoods_id());
		goodsQuantity.setEnable_quantity(goods.getReturn_num());
		goodsQuantity.setQuantity(goods.getReturn_num());
		boolean quantity = goodsQuantityManager.addGoodsQuantity(goodsQuantity);
		if (!quantity) {
			throw new NoPermissionException(ErrorCode.INVALID_REQUEST_PARAMETER, "商品入库失败，请刷新后重新操作");
		}
		// 发送入库的消息
		RefundChangeMessage refundStatusChangeMessage = new RefundChangeMessage(refund,
				RefundChangeMessage.SELLER_IN_STOCK);
		amqpTemplate.convertAndSend(AmqpExchange.REFUND_STATUS_CHANGE.name(), "refund-apply-routingKey",
				refundStatusChangeMessage);
		return stockIn;
	}

	/**
	 * 记录操作日志
	 * 
	 * @param sn
	 * @param operator
	 * @param detail
	 */
	private void log(String sn, String operator, String detail) {
		RefundLog refundLog = new RefundLog();
		refundLog.setOperator(operator);
		refundLog.setRefund_sn(sn);
		refundLog.setLogtime(DateUtil.getDateline());
		refundLog.setLogdetail(detail);

		this.daoSupport.insert("es_refund_log", refundLog);

	}

	/**
	 * 进行操作校验 看此状态下是否允许此操作
	 * 
	 * @param order
	 * @param orderOperate
	 */
	private void checkAllowable(RefundVo refund, RefundOperate refundOperate) {

		String status = refund.getRefund_status();// 退款当前流程状态
		RefundStatus refund_status = RefundStatus.valueOf(status);

		String refuse_type = refund.getRefuse_type();// 退货/退款
		RefuseType type = RefuseType.valueOf(refuse_type);

		boolean allowble = RefundOperateChecker.checkAllowable(type, refund_status, refundOperate);

		if (!allowble) {
			throw new NoPermissionException("",
					"订单" + refund_status.description() + "状态不能进行" + refundOperate.description() + "操作");
		}

	}

	@Override
	public RefundVo cancelRefund(String sn) {
		RefundVo refund = this.daoSupport.queryForObject("select * from es_refund where sn =?", RefundVo.class, sn);
		if (refund == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "退货（款）单[" + sn + "]找不到");
		}

		this.checkAllowable(refund, RefundOperate.cancel);

		this.daoSupport.execute("update es_refund set refund_status=? where sn =?", RefundStatus.cancel.value(), sn);

		return refund;
	}

	@Override
	public List<RefundPartVo> queryNoReturnOrder() {

		String sql = "select *,sn refund_sn from es_refund where refund_status = ? and refund_way = 'original'";

		List<RefundPartVo> list = this.daoSupport.queryForList(sql, RefundStatus.refunding.value());

		return list;
	}

	@Override
	public void update(List<RefundPartVo> list) {

		if (list != null && list.size() > 0) {
			for (RefundPartVo refund : list) {
				Map map = new HashedMap();
				map.put("refund_status", refund.getRefund_status());
				this.daoSupport.update("es_refund", map, "sn = " + refund.getRefund_sn());
			}
		}

	}

}
