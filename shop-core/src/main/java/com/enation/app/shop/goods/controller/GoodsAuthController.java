package com.enation.app.shop.goods.controller;

import com.enation.eop.SystemSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.service.IGoodsAuthManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Scope("prototype")
@RestController
@RequestMapping("/shop/admin/goods/auth")
public class GoodsAuthController extends GridController {
	@Autowired
	private IGoodsAuthManager goodsAuthManager;
	@Autowired
	private IGoodsManager goodsManager;

	/**
	 * 跳转至审核商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list-page")
	public ModelAndView authList() {
		ModelAndView view = this.getGridModelAndView();
		view.addObject("domain", SystemSetting.getPrimary_domain());

		view.setViewName("/b2b2c/admin/goods/auth/auth_list");
		return view;
	}

	/***
	 * 跳转至审核商品详情页
	 * 
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/input")
	public ModelAndView authInput(Integer goodsId) {
		ModelAndView view = new ModelAndView();
		view.addObject("storeGoods", goodsManager.getFromDB(goodsId));

		view.setViewName("/b2b2c/admin/goods/auth/auth_input");
		return view;
	}

	/**
	 * 审核商品列表
	 * 
	 * @return
	 */
	@ApiOperation(value = "查询审核商品列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query") })
	@GetMapping(value = "/list")
	public GridJsonResult list(GoodsQueryParam goodsQueryParam) {
		if (goodsQueryParam.getStype() == null) {
			goodsQueryParam.setStype(0);
		}
		goodsQueryParam.setPage_no(this.getPage());
		goodsQueryParam.setPage_size(this.getPageSize());
		Page page = this.goodsAuthManager.getList(goodsQueryParam);
		return JsonResultUtil.getGridJson(page);
	}

	/**
	 * 审核商品操作
	 * 
	 * @return
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "pass", value = "是否通过审核", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "message", value = "审核备注", required = true, dataType = "int", paramType = "query") })
	@PostMapping(value = "/pass")
	public void pass(Integer goodsId, Integer pass, String message) {
		goodsAuthManager.authGoods(goodsId, pass, message);
	}
}
