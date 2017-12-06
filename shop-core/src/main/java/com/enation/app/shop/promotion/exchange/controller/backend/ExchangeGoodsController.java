package com.enation.app.shop.promotion.exchange.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.promotion.exchange.service.IExchangeGoodsManager;
import com.enation.eop.SystemSetting;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.google.common.base.Optional;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
/**
 *后台积分商品列表
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月26日 上午11:25:09
 */
@Api(description = "后台积分商品列表")
@RestController
@RequestMapping("/shop/admin/exchange-goods")
public class ExchangeGoodsController extends GridController{
	@Autowired
	private IExchangeGoodsManager exchangeGoodsManager;
	/**
	 * 跳转积分商品列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/list-goods")
	public ModelAndView listCat() {
		ModelAndView view = new ModelAndView();
		view.addObject("domain",SystemSetting.getPrimary_domain());
		view.setViewName("/shop/admin/exchange/goods_list");
		return view;
	}
	/**
	 * 积分商品列表页json
	 * @param goodsQueryParam 参数vo
	 * @return
	 */
	@ApiOperation(value = "积分商品列表页json")
	@GetMapping
	public GridJsonResult listJson(GoodsQueryParam goodsQueryParam) {
		Optional<Integer> fullName = Optional.fromNullable( goodsQueryParam.getStype());
		boolean stype= fullName.isPresent();
		if(!stype){
			goodsQueryParam.setStype(0);
		}
		goodsQueryParam.setPage_no(this.getPage());
		goodsQueryParam.setPage_size(this.getPageSize());
		Page page = exchangeGoodsManager.list(goodsQueryParam);
		return JsonResultUtil.getGridJson(page);
	}
	/**
	 * 积分商品排序
	 * @param goods_id  商品数组
	 * @param sord  排序数组
	 * @return
	 */
	@ApiOperation(value = "后台积分商品列表排序")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "goods_id", value = "商品id数组", required = true, dataType = "Integer" ,paramType="query"),
        @ApiImplicitParam(name = "sord", value = "排序数字数组", required = true, dataType = "Integer" ,paramType="query"),
	})
	@GetMapping(value="/update-sort")
	public void updateSort(Integer[] goods_id, Integer[] sord) {
			this.exchangeGoodsManager.updateSort(goods_id, sord);
	}
}
