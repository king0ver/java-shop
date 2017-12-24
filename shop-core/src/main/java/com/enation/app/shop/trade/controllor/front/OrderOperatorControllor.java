package com.enation.app.shop.trade.controllor.front;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.app.shop.trade.model.enums.OrderPermission;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.app.shop.trade.model.vo.OrderConsigneeVo;
import com.enation.app.shop.trade.model.vo.operator.Cancel;
import com.enation.app.shop.trade.model.vo.operator.Complete;
import com.enation.app.shop.trade.model.vo.operator.Confirm;
import com.enation.app.shop.trade.model.vo.operator.Delivery;
import com.enation.app.shop.trade.model.vo.operator.Rog;
import com.enation.app.shop.trade.service.IOrderOperateManager;
import com.enation.framework.validator.NoPermissionException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 订单流程操作控制器
 * @version 1.0
 * @since 6.4
 * create by 2017-08-21
 * @author kingapex
 *
 */
@Api(description="订单操作API")
@RestController  
@RequestMapping("/order-opration")
@Validated
public class OrderOperatorControllor {
	
	@Autowired
	private IOrderOperateManager orderOperateManager;

	@Autowired
	private ISellerManager sellerManager;
	
	
	@ApiOperation(value="确认订单", notes="确认商家的订单")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path"),
        @ApiImplicitParam(name = "is_agree", value = "是否同意确认订单", required =true, dataType = "boolean" ,paramType="query" ),
	 })
	@ResponseBody
	@PostMapping(value="/seller/order/confirm/{ordersn}")
	public Confirm confirm(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn, @NotNull(message= "必须指定是否同意确认此订单") Boolean is_agree){

		Seller seller = sellerManager.getSeller();

		if(seller==null){
			throw new NoPermissionException("","无权确认此订单");
		}
		
		//默认是同意
		if( is_agree==null ){
			is_agree=true;
		}
		
		Confirm confirm  = new Confirm();
		confirm.setIs_agree(is_agree);
		confirm.setOrder_sn(ordersn);
		confirm.setOperator("店铺："+seller.getUname());
		this.orderOperateManager.confirm(confirm, OrderPermission.seller);
		
		return confirm;
	}
	
	
	@ApiOperation(value="订单发货", notes="商家对某订单执行发货操作")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path"),
        @ApiImplicitParam(name = "ship_no", value = "发货单号", required =true, dataType = "String" ,paramType="query" ),
	 })
	@ResponseBody
	@PostMapping(value="/seller/order/delivery/{ordersn}")
	public Delivery ship(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn, 
			@NotNull(message= "必须输入发货单号") String ship_no,
			@NotNull(message= "必须选择物流公司") int logi_id,
			@NotNull(message= "必须选择物流公司") String logi_name){

		Seller seller = sellerManager.getSeller();
		if(seller==null){
			throw new NoPermissionException("","无权操作此订单");
		}
	 
		Delivery delivery = new Delivery();
		delivery.setDelivery_no(ship_no);
		delivery.setOrder_sn(ordersn);
		delivery.setLogi_id(logi_id);
		delivery.setLogi_name(logi_name);
		delivery.setOperator( "店铺:"+seller.getUname());
		orderOperateManager.ship( delivery,  OrderPermission.seller );
		
		return delivery;
	}
	
	
	@ApiOperation(value="订单确认收货", notes="买家对某订单执行收货操作")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path")
	 })
	@ResponseBody
	@PostMapping(value="/mine/order/rog/{ordersn}")
	public Rog rog(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn){
	 
		Rog rog = new Rog();
		rog.setOrder_sn(ordersn);
		rog.setOperator("买家");
		orderOperateManager.rog( rog,  OrderPermission.buyer );
		
		return rog;
	}
	
	
	
	@ApiOperation(value="订单完成", notes="买家对某订单执行完成操作")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path")
	 })
	@ResponseBody
	@PostMapping(value="/seller/order/complete/{ordersn}")
	public Complete complete(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn){
	 
		Complete complete = new Complete();
		complete.setOrder_sn(ordersn);
		complete.setOperator("系统");
		orderOperateManager.complete( complete,  OrderPermission.seller );
		
		return complete;
	}
	
	
	

	@ApiOperation(value="取消订单", notes="买家对某订单执行取消操作")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path"),
        @ApiImplicitParam(name = "reson", value = "取消原因", required = true, dataType = "String",paramType="query")
	 })
	@ResponseBody
	@PostMapping(value="/seller/order/cancel/{ordersn}")
	public Cancel cancel(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn, @NotNull(message= "必须指定取消原因") String reson){
	 
		Cancel cancel = new Cancel();
		cancel.setOrder_sn(ordersn);
		cancel.setOperator("卖家");
		cancel.setReson(reson);
		orderOperateManager.cancel(cancel, OrderPermission.seller );
		return cancel;
	}

	@ApiOperation(value="取消订单", notes="买家对某订单执行取消操作")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path"),
			@ApiImplicitParam(name = "reson", value = "取消原因", required = true, dataType = "String",paramType="query")
	})
	@ResponseBody
	@PostMapping(value="/mine/order/cancel/{ordersn}")
	public Cancel meinCancel(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn, @NotNull(message= "必须指定取消原因") String reson){

		Cancel cancel = new Cancel();
		cancel.setOrder_sn(ordersn);
		cancel.setOperator("买家");
		cancel.setReson(reson);
		orderOperateManager.cancel(cancel, OrderPermission.buyer );
		return cancel;
	}






//	@ApiOperation(value="client确认支付订单", notes="client确支付订单")
//	@ApiImplicitParams({
//        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="query"),
//        @ApiImplicitParam(name = "payprice", value = "付款金额", required = true, dataType = "Double",paramType="query")
//	 })
//	@ResponseBody
//	@RequestMapping(value="/client/order/pay",method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
//	public OrderPo payOrder(@NotNull(message= "必须指定订单编号") String ordersn, @NotNull(message= "必须指定付款金额")  Double payprice){
//
//		OrderPo po = this.orderOperateManager.payOrder(ordersn,payprice,OrderPermission.client);
//
//		return po;
//	}
//
//
//
//	@ApiIgnore
//	@ApiOperation(value="client确支付交易", notes="client确支付交易")
//	@RequestMapping(value="/client/trade/pay",method=RequestMethod.POST,consumes="application/json")
//	public String payTrade(@RequestBody OrderPayReturnParam param){
//
//		this.orderOperateManager.payTrade(param,OrderPermission.client);
//
//		return "";
//	}
	
	@ApiOperation(value="admin取消订单",notes="管理员对某订单执行取消操作")
	@ApiImplicitParams({
		 @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="path"),
		 @ApiImplicitParam(name = "reson", value = "取消原因", required = true, dataType = "String",paramType="query")
	 })
	@ResponseBody
	@PostMapping(value="/admin/order/cancel/{ordersn}")
	public Cancel adminCancel(@NotNull(message= "必须指定订单编号") @PathVariable(name="ordersn") String ordersn,@NotNull(message= "必须指定取消原因") String reson){
	 
		Cancel cancel = new Cancel();
		cancel.setOrder_sn(ordersn);
		cancel.setOperator("管理员");
		cancel.setReson(reson);
		orderOperateManager.cancel(cancel, OrderPermission.admin );
		return cancel;
	}
	
	@ApiOperation(value="admin确认收款",notes="管理员对某订单执行取消操作")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "ordersn", value = "订单编号", required = true, dataType = "String",paramType="query"),
        @ApiImplicitParam(name = "payprice", value = "付款金额", required = true, dataType = "Double",paramType="query")
	 })
	@ResponseBody
	@RequestMapping(value="/admin/order/pay",method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderPo adminPayOrder(@NotNull(message= "必须指定订单编号")  String ordersn,  @NotNull(message= "必须指定付款金额")  Double payprice){
	 
		OrderPo po = this.orderOperateManager.payOrder(ordersn,payprice,OrderPermission.admin);
		
		return po;
	}
	

	@ApiOperation(value="商家修改收货人地址",notes="商家发货前，可以修改收货人地址信息")
	@RequestMapping(value="/seller/order/{order_sn}/address",method=RequestMethod.POST)
	public OrderConsigneeVo updateOrderConsignee(@PathVariable String order_sn,OrderConsigneeVo orderConsignee){
	 
		orderConsignee.setSn(order_sn);
		orderConsignee  = this.orderOperateManager.updateOrderConsignee(orderConsignee);
		
		return orderConsignee;
	}
	
	@ApiOperation(value="商家修改订单价格",notes="买家付款前可以修改订单价格")
	@RequestMapping(value="/seller/order/{order_sn}/price",method=RequestMethod.POST)
	public String updateOrderPrice(@PathVariable String order_sn,Double order_price){
		
		this.orderOperateManager.updateOrderPrice(order_sn,order_price);
		
		return null;
	}

}
