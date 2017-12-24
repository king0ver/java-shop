package com.enation.app.shop.orderbill.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.orderbill.model.enums.BillStatusEnum;
import com.enation.app.shop.orderbill.model.po.Bill;
import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.ResourceNotFoundException;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 结算单Controller
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午2:21:28
 */
@Api(description = "结算单Api")
@RestController
@RequestMapping("/order-bill")
@Validated
public class BillController extends GridController {
	
	@Autowired
	private IBillManager billManager;
	@Autowired
	private ISellerManager SellerMangager;
	
	/**
	 * 卖家对账单进行下一步操作
	 * @param bill_id 账单id
	 * @return status 账单状态值
	 */
	@ApiOperation(value = "卖家对账单进行下一步操作", response = BillItem.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "bill_id", value = "账单id", required = true, dataType = "int", paramType = "query"),
	})
	@RequestMapping(value = "/seller/bill/next",method = RequestMethod.POST)
	public Integer nextBill(@NotNull(message = "账单id不能为空") Integer bill_id) {
		//判断卖家是否登录
		Seller member = SellerMangager.getSeller();
		if (member == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		
		Integer sellerId = member.getStore_id();
		
		Bill bill = this.billManager.getBill(bill_id, sellerId);
		
		if (bill == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "未找到此账单");
		}
		
		Integer status = bill.getStatus();
		
		// 如果状态是已出账状态
		if (status.intValue() == BillStatusEnum.OUT.getIndex()) {
			
			status = BillStatusEnum.RECON.getIndex();
			// 对账成功
			this.billManager.editStatus(bill_id, status);
			
		// 如果状态是已付款状态
		} else if (status.intValue() == BillStatusEnum.PAY.getIndex()) {
			
			status = BillStatusEnum.COMPLETE.getIndex();
			// 确认完成
			this.billManager.editStatus(bill_id, status);
			
		} 
		
		return status;
	}
	/**
	 * 管理员对账单进行下一步操作
	 * @param bill_id 账单id
	 * @param seller_id 店铺id
	 * @return 1
	 */
	@ApiOperation(value = "管理员对账单进行下一步操作", response = BillItem.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "bill_id", value = "账单id", required = true, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "seller_id", value = "店铺id", required = true, dataType = "int", paramType = "query")
	})
	@RequestMapping(value = "/admin/bill/next",method = RequestMethod.POST)
	public Integer nextBill(@NotNull(message = "账单id不能为空") Integer bill_id, @NotNull(message = "店铺id不能为空") Integer seller_id) {
			
		Bill bill = this.billManager.getBill(bill_id, seller_id);
		
		if (bill == null) {
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "未找到此账单");
		}
		
		Integer status = bill.getStatus();
		
		//已对账
		if (status.intValue() == BillStatusEnum.RECON.getIndex()) {
			
			// 审核需要后台的权限校验
			if (isHaveAuth(BillStatusEnum.PASS.getIndex())) {
				// 审核通过
				this.billManager.editStatus(bill_id, BillStatusEnum.PASS.getIndex());
			}
			
		// 如果状态是已通过审核状态
		} else if (status.intValue() == BillStatusEnum.PASS.getIndex()) {
			
			// 付款需要后台的权限校验
			if (isHaveAuth(BillStatusEnum.PAY.getIndex())) {
				// 付款成功
				this.billManager.editBillPayStatus(bill_id);
				
			}
			
		} 
		
		return 1;
	}
	
	/**
	 * 商家查询我的结算单列表
	 * @param page_no 页码
	 * @param page_size 每页数量
	 * @return Page<Bill>
	 */
	@ApiOperation(value = "商家查询我的结算单列表")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query")
	})
	@RequestMapping(value = "/seller/bill",method = RequestMethod.GET)
	public GridJsonResult getMyBill(Integer page_no,Integer page_size){
		
		Page<Bill> list = this.billManager.queryMyShopBill(page_no,page_size);
		
		return JsonResultUtil.getGridJson(list);
	}
	
	/**
	 * 管理员查询所有周期结算单列表统计
	 * @param page_no 页码
	 * @param page_size 每页数量
	 * @return Page<Bill>
	 */
	@ApiOperation(value = "管理员查询所有周期结算单列表统计")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query")
	})
	@RequestMapping(value = "/admin/bill-all",method = RequestMethod.GET)
	public GridJsonResult getAllBill(){
		
		Page<Bill> list = this.billManager.getAllBill(this.getPage(),this.getPageSize());
		
		return JsonResultUtil.getGridJson(list);
	}
	/**
	 * 管理员查询所有周期结算单列表详细
	 * @param page_no 页码
	 * @param page_size 每页数量
	 * @param sn 账单号
	 * @return Page<Bill>
	 */
	@ApiOperation(value = "管理员查询所有周期结算单列表详细")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "sn", value = "账单号", required = false, dataType = "int", paramType = "query")
	})
	@RequestMapping(value = "/admin/bill",method = RequestMethod.GET)
	public GridJsonResult getBillDetail(Integer page_no,Integer page_size,String sn){
		
		Page<Bill> list = this.billManager.getBillDetail(this.getPage(),this.getPageSize(),sn);
		
		return JsonResultUtil.getGridJson(list);
	}
	/**
	 * 管理员查询某账单详细
	 * @param id 账单id
	 * @return bill 对象
	 */
	@ApiOperation(value = "管理员查询某账单详细")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "id", value = "账单id", required = false, dataType = "int", paramType = "path"),
	})
	@RequestMapping(value = "/admin/bill/{id}",method = RequestMethod.GET)
	public Bill getBillDetail(@PathVariable Integer id){
		
		Bill bill = this.billManager.get(id);
				
		return bill;
	}
	
	/**
	 * 判断是否管理员操作权限，有些审核是不能通过直接访问API来执行的 
	 * @return 临时写死 true
	 */
	private boolean isHaveAuth(Integer status){
		
		//TODO 临时写死,  
		return true;
	}
	
}
