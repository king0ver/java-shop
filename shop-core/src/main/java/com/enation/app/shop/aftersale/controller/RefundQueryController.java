package com.enation.app.shop.aftersale.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.aftersale.model.vo.RefundPartVo;
import com.enation.app.shop.aftersale.model.vo.RefundQueryParam;
import com.enation.app.shop.aftersale.model.vo.RefundVo;
import com.enation.app.shop.aftersale.service.IAfterSaleManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 退款（货）单控制器
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年4月14日下午12:45:43
 */
@Api(description = "退货（款单）查询API")
@RestController
@RequestMapping("/after-sale")
@Validated
public class RefundQueryController  extends GridController {

	@Autowired
	private IAfterSaleManager afterSaleManager;

	
	@ApiOperation(value = "管理员查询全部退货/退款单")
	@GetMapping(value = "/admin/refund-all")
	public GridJsonResult listReturnByAdmin(RefundQueryParam queryParam) {
		
		AdminUser adminUser  = UserConext.getCurrentAdminUser();
		
		if(adminUser == null){
			throw new NoPermissionException( "无权访问");
		}
		queryParam.setPage_no(this.getPage());
		queryParam.setPage_size(this.getPageSize());
		Page<RefundVo> page = this.afterSaleManager.query(queryParam);

		return JsonResultUtil.getGridJson(page);
	}
	
	/**
	 * 查询退款中的退款单
	 * @return
	 */
	@ApiIgnore
	@GetMapping(value = "/client/refund/refunding")
	public List<RefundPartVo> queryRefund(){
		
		List<RefundPartVo> list = afterSaleManager.queryNoReturnOrder();
		
		return list;
	}
	
}
