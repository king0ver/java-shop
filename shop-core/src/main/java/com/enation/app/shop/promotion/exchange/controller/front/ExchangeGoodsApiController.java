package com.enation.app.shop.promotion.exchange.controller.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.promotion.exchange.service.IExchangeGoodsManager;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 前台积分商城获取商品
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月27日 下午3:15:25
 */
@Api(description = "前台积分商城获取商品")
@RestController
@RequestMapping("/shop/mine/exchange-goods")
public class ExchangeGoodsApiController {
	@Autowired
	private IExchangeGoodsManager exchangeGoodsManager;

	/**
	 * 全部积分商品
	 * 
	 * @param page_no
	 * @param cat_id
	 *            分类id
	 * @return
	 */
	@GetMapping
	@ApiOperation(value = "根据分类id获取商品，每页大小15个商品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cat_id", value = "分类id", required = true, dataType = "int", paramType = "query") })
	public GridJsonResult getGoods(Integer page_no, Integer cat_id) {
		int pageSize = 15;
		Page page = exchangeGoodsManager.frontList(page_no, pageSize, cat_id);
		return JsonResultUtil.getGridJson(page);
	}
}
