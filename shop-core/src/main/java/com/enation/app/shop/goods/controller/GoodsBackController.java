package com.enation.app.shop.goods.controller;


import com.enation.eop.SystemSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.service.IBrandManager;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.validator.UnProccessableServiceException;
import com.google.common.base.Optional;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 后台商品类表controller 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月19日 下午3:59:45
 */
@Scope("prototype")
@RestController
@RequestMapping("/shop/admin/goods")
public class GoodsBackController extends GridController{
	@Autowired
	protected IBrandManager brandManager;
	@Autowired
	private IGoodsManager goodsManager;
	/**
	 * 商品列表
	 * 
	 * @param brand_id
	 *            品牌Id,Integer
	 * @param catid
	 *            商品分类Id,Integer
	 * @param name
	 *            商品名称,String
	 * @param sn
	 *            商品编号,String
	 * @param tagids
	 *            商品标签Id,Integer[]
	 * @return 商品列表页
	 */
	@RequestMapping(value = "/list")
	public ModelAndView list() {
		ModelAndView view = this.getGridModelAndView();
		String market_enable = ThreadContextHolder.getHttpRequest().getParameter("market_enable");
		view.addObject("brandList", brandManager.list());
		view.addObject("optype", "no");
		view.addObject("market_enable", market_enable);
		view.addObject("domain",SystemSetting.getPrimary_domain());
		view.setViewName("/b2b2c/admin/goods/goods_list");

		return view;
	}
	/**
	 * 跳转至商品下架详情页
	 * @param goodsId
	 * @return
	 */
	@RequestMapping(value = "/input")
	public ModelAndView input(Integer goodsId) {
		ModelAndView view = new ModelAndView();
		view.addObject("goods", goodsManager.getFromDB(goodsId));
		view.setViewName("/b2b2c/admin/goods/goods_input");
		return view;
	}
	/**
	 * 商品列表页json
	 * @param goodsQueryParam 参数vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/list-json")
	public GridJsonResult listJson(GoodsQueryParam goodsQueryParam) {
		Optional<Integer> fullName = Optional.fromNullable( goodsQueryParam.getStype());
		boolean stype= fullName.isPresent();
		if(!stype){
			goodsQueryParam.setStype(0);
		}
		goodsQueryParam.setPage_no(this.getPage());
		goodsQueryParam.setPage_size(this.getPageSize());
		Page page = goodsManager.list(goodsQueryParam);
		return JsonResultUtil.getGridJson(page);
	}
	/**
	 *  管理员下架商品
	 * @param goods_ids
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "管理员下架商品",notes = "管理员下架商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goodsId",value="商品ID集合",required=true,paramType="query",dataType="int",allowMultiple=true),
		@ApiImplicitParam(name="message",value="商品下架原因",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value = "/under" ,method = RequestMethod.POST)
	public void underGoods(Integer[] goodsId,String message) throws Exception{
		AdminUser user = UserConext.getCurrentAdminUser();
		if(user == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		if(goodsId!=null){
			goodsManager.under(goodsId,message);
		}
	}
	/**
	 *  管理员上架商品
	 * @param goods_ids
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "管理员上架商品",notes = "管理员上架商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_ids",value="商品ID集合",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value = "/up" ,method = RequestMethod.POST)
	public void unpGoods(Integer[] goods_ids) throws Exception{
		AdminUser user = UserConext.getCurrentAdminUser();
		if(user == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		if(goods_ids!=null){
			goodsManager.up(goods_ids);
		}
	}
	
}
