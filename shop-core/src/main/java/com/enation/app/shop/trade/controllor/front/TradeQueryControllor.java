package com.enation.app.shop.trade.controllor.front;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.trade.model.po.TradePo;
import com.enation.app.shop.trade.service.IOrderQueryManager;
import com.enation.app.shop.trade.service.ITradeQueryManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交易控制器
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年4月6日下午8:15:27
 */
@Api(description="交易查询API")
@RestController
@RequestMapping("/order-query")
public class TradeQueryControllor{
	
	@Autowired
	private ITradeQueryManager tradeQueryManager;
	
	@Autowired
	private IOrderQueryManager orderQueryManager;

	
	@ApiOperation(value="会员根据编号获取交易", notes="会员根据编号获取交易")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "tradesn", value = "订单编号", required = true, dataType = "String",paramType="query")
	 })
	@ResponseBody
	@RequestMapping(value="/mine/trade/{tradesn}",method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public TradePo mineGetBySn(@NotNull(message= "必须指定编号")  @PathVariable(name="tradesn")   String  tradesn ){


		Member member  = UserConext.getCurrentMember();
		
		if(   member ==null  || member.getMember_id()==null  ) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		TradePo po  = this.tradeQueryManager.getOneBySn(tradesn);
		
		
		int memberid = po.getMember_id();
		if(memberid != member.getMember_id().intValue()){
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "无权访问此订单");
		}
		
		return  po;
	}
	
	
}
