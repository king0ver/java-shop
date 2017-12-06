package com.enation.app.shop.promotion.exchange.controller.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.promotion.exchange.model.vo.ExchangeGoodsCategoryVo;
import com.enation.app.shop.promotion.exchange.service.IExchangeCategoryManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 积分商城前端api 积分首页需要使用
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月31日 上午11:54:51
 */
@Api(description = "积分商城前端api 积分首页需要使用")
@RestController
@RequestMapping("/api/shop/exchange")
public class ExchangeApiController {
	@Autowired
	private IExchangeCategoryManager exchangeCatManger;

	/**
	 * 获取所有子分类数据
	 * 
	 * @return
	 */
	@ApiOperation(value = "获取所有积分商品分类数据")
	@RequestMapping(value = "/add-list-json", produces = MediaType.APPLICATION_JSON_VALUE)
	public List addListJson() {
		List<ExchangeGoodsCategoryVo> addlist = exchangeCatManger.listAllChildren(0);
		return addlist;
	}

	/**
	 * 检查积分
	 * 
	 * @return
	 */
	@ApiOperation(value = "检查积分")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "exchange_point", value = "积分", required = true, dataType = "Integer", paramType = "query") })
	@RequestMapping(value = "/check-point", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult checkPoint(Integer exchange_point) {
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			return JsonResultUtil.getErrorJson("未登录不能购买积分商品");
		}
		String checkPoint = "false";
		if (member.getMp() >= exchange_point) {
			checkPoint = "true";
		}
		return JsonResultUtil.getObjectJson(checkPoint);
	}

}
