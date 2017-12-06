package com.enation.app.shop.orderbill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillItemManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.annotation.PageNo;
import com.enation.framework.annotation.PageSize;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 结算单项Controller
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 上午10:21:59
 */
@Api(description = "结算单项Api")
@RestController
@RequestMapping("/order-bill")
@Validated
public class BillItemApiController  extends GridController {

	@Autowired
	private IBillItemManager billItemManager;
	/**
	 * 查询结算单的具体订单项
	 * @param bill_id 结算单id
	 * @param item_type 订单类型,0:收款，1:退款
	 * @param page_no 页码
	 * @param page_size 每页数量
	 * @return Page<BillItem>
	 */
	@ApiOperation(value = "查询结算单的具体订单项", response = BillItem.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "bill_id", value = "结算单id", required = true, dataType = "string", paramType = "path"),
		@ApiImplicitParam(name = "item_type", value = "订单类型,0:收款，1:退款", required = true, dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "page_no", value = "页码", required = false, dataType = "int", paramType = "query"),
		@ApiImplicitParam(name = "page_size", value = "每页数量", required = false, dataType = "int", paramType = "query")
	})
	@RequestMapping(value = "/admin/bill/{bill_id}/item",method = RequestMethod.GET)
	public GridJsonResult getItemList(@PathVariable Integer bill_id,Integer item_type,@PageNo Integer page_no,@PageSize Integer page_size) {
		
		Page<BillItem> page = billItemManager.getItemList(bill_id,item_type,this.getPage(),this.getPageSize());
		
		return JsonResultUtil.getGridJson(page);
	}
	

}
