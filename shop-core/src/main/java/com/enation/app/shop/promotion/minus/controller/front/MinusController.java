package com.enation.app.shop.promotion.minus.controller.front;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.promotion.minus.model.vo.MinusVo;
import com.enation.app.shop.promotion.minus.service.IMinusManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

/**
 * 单品立减活动controller
 * 
 * @author mengyuanming
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月18日下午8:04:17
 *
 */
@Api(description = "单品立减API")
@RestController
@RequestMapping("/api/shop/promotion/minus")
public class MinusController extends GridController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IMinusManager minusManager;

	@Autowired
	private ISellerManager sellerManager;

	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;

	/**
	 * 获取单品立减商品列表
	 * 
	 * @return List 返回的单品立减活动列表
	 */
	@ResponseBody
	@GetMapping("/list")
	public List list() {
		return this.minusManager.list();
	}

	/**
	 * 分页获取单品立减商品列表
	 * 
	 * @return Page 返回的分页列表
	 */
	@ResponseBody
	@ApiImplicitParams({
			@ApiImplicitParam(name = "keyword", value = "按活动title搜索", required = false, dataType = "String", paramType = "path"),
			@ApiImplicitParam(name = "shop_id", value = "店铺id", required = true, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "pageNo", value = "页面起始页码", required = false, dataType = "Integer", paramType = "path"),
			@ApiImplicitParam(name = "pageSize", value = "每页显示数量", required = false, dataType = "Integer", paramType = "path") })
	@GetMapping("/list-json")
	public GridJsonResult list_json(String keyword, Integer shop_id, Integer pageNo, Integer pageSize) {
		if (pageNo == null) {
			pageNo = this.getPage();
		}
		if (pageSize == null) {
			pageSize = this.getPageSize();
		}
		Page minusPage = this.minusManager.listJson(keyword, shop_id, pageNo, pageSize);
		return JsonResultUtil.getGridJson(minusPage);
	}

	/**
	 * 增加单品立减商品
	 * 
	 * @param minusVo
	 * @return JsonResult 成功或错误信息
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "minus_id", value = "活动id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "title", value = "活动标题", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "活动开始时间戳", required = false, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "活动结束时间戳", required = false, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "start_time_str", value = "活动开始时间", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "end_time_str", value = "活动结束时间", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "minus_price", value = "活动减免金额", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "description", value = "活动描述", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "range_type", value = "是否全部商品参与", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "disabled", value = "是否停用", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "shop_id", value = "商家id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "goodsList", value = "商品列表", required = false, dataType = "List", paramType = "query") })
	@RequestMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult add(@RequestBody MinusVo minusVo) {
		try {

			Long start_time = DateUtil.getDateline(minusVo.getStart_time_str(), "yyyy-MM-dd HH:mm:ss");
		    Long end_time = DateUtil.getDateline(minusVo.getEnd_time_str(), "yyyy-MM-dd HH:mm:ss");
		    minusVo.setStart_time(start_time);
		    minusVo.setEnd_time(end_time);

			// 开始时间不能大于结束时间
			if (start_time > end_time ) {
				return JsonResultUtil.getErrorJson("活动开始时间不能大于结束时间");
			}

			// 如果促销活动选择的是部分商品参加活动
			if (minusVo.getRange_type().equals(2)) {
				// 商品id组不能为空
				if (minusVo.getGoodsList() == null) {
					return JsonResultUtil.getErrorJson("请选择要参与活动的商品");
				}
			}
			// 减少的现金不能为0
			if (minusVo.getMinus_price() == 0) {
				return JsonResultUtil.getErrorJson("减少的现金不能为0");
			}
			
			// 判断同一时间内一件商品是否有两个活动
			List<Map<String,String>> list = this.minusManager.isRepetition(minusVo,start_time,end_time);
			if (list.size() > 0) {
				ThreadContextHolder.getSession().setAttribute("minusConflictGoods", list);
				JsonResult jsonRetult = new JsonResult();
				jsonRetult.setData(list);
				jsonRetult.setResult(0);
				return jsonRetult;
			}

			this.minusManager.add(minusVo);
			return JsonResultUtil.getSuccessJson("新增促销活动成功");

		} catch (Exception e) {
			this.logger.error("新增促销活动失败:", e);
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("新增促销活动失败");
		}
	}

	/**
	 * 修改单品立减商品
	 * 
	 * @param minusVo
	 * @return JsonResult 成功或错误信息
	 */
	@ApiImplicitParams({
			@ApiImplicitParam(name = "minus_id", value = "活动id", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "title", value = "活动标题", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "start_time", value = "活动开始时间戳", required = false, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "end_time", value = "活动结束时间戳", required = false, dataType = "long", paramType = "query"),
			@ApiImplicitParam(name = "start_time_str", value = "活动开始时间", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "end_time_str", value = "活动结束时间", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "minus_price", value = "活动减免金额", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "description", value = "活动描述", required = false, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "range_type", value = "是否全部商品参与", required = true, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "disabled", value = "是否停用", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "shop_id", value = "商家id", required = false, dataType = "Integer", paramType = "query"),
			@ApiImplicitParam(name = "goodsList", value = "商品列表", required = false, dataType = "List", paramType = "query") })
	@RequestMapping(value = "/edit", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult edit(@RequestBody MinusVo minusVo) {
		try {
			// 获取当前登陆的店铺ID
			Integer store_id = sellerManager.getSeller().getStore_id();

			// 增加权限
			if (minusVo.getShop_id() == null || !minusVo.getShop_id().equals(store_id)) {
				return JsonResultUtil.getErrorJson("您没有权限");
			}

			Long start_time = DateUtil.getDateline(minusVo.getStart_time_str(), "yyyy-MM-dd HH:mm:ss");
		    Long end_time = DateUtil.getDateline(minusVo.getEnd_time_str(), "yyyy-MM-dd HH:mm:ss");
		    minusVo.setStart_time(start_time);
		    minusVo.setEnd_time(end_time);
			// 开始时间不能大于结束时间
			if (start_time > end_time ) {
				return JsonResultUtil.getErrorJson("活动开始时间不能大于结束时间");
			}

			// 活动时间只能增加，不能缩短
			if (this.minusManager.checkTime(minusVo)) {
				return JsonResultUtil.getErrorJson("新的活动时间，开始时间不能大于过去的开始时间，结束时间不能小于过去的结束时间");
			}
			
			// 判断活动是否过期
			if (this.minusManager.isOutOfDate(minusVo.getMinus_id())) {
				return JsonResultUtil.getErrorJson("活动尚在进行中，不可修改");
			}
			
			//活动失效后，不可编辑
			if (this.minusManager.isAfterTime(minusVo.getMinus_id())){
				return JsonResultUtil.getErrorJson("活动已失效，不可编辑");
			}

			// 如果促销活动选择的是部分商品参加活动
			if (minusVo.getRange_type().equals(2)) {
				// 商品id组不能为空
				if (minusVo.getGoodsList() == null) {
					return JsonResultUtil.getErrorJson("请选择要参与活动的商品");
				}
			}

			// 减少的现金不能为0
			if (minusVo.getMinus_price() == 0) {
				return JsonResultUtil.getErrorJson("减少的现金不能为0");
			}

			// 删除过去的商品
			this.promotionGoodsManager.delete(minusVo.getMinus_id(), PromotionTypeEnum.MINUS.getType());

			// 判断同一时间内一件商品是否有两个活动
			List list = this.minusManager.isRepetition(minusVo,start_time,end_time);
			if (list.size() > 0) {
				ThreadContextHolder.getSession().setAttribute("minusConflictGoods", list);
				JsonResult jsonRetult = new JsonResult();
				jsonRetult.setData(list);
				jsonRetult.setResult(0);
				return jsonRetult;
			}

			this.minusManager.edit(minusVo);
			return JsonResultUtil.getSuccessJson("修改促销活动成功！");
		} catch (Exception e) {
			this.logger.error("修改促销活动失败:", e);
			return JsonResultUtil.getErrorJson("修改促销活动失败！");
		}
	}

	/**
	 * 删除单品立减商品
	 * 
	 * @param minus_id
	 *            活动id
	 * @return JsonResult 成功或错误信息
	 */
	@ResponseBody
	@ApiImplicitParam(name = "minus_id", value = "单品立减对象", required = true, dataType = "Integer", paramType = "path")
	@RequestMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public JsonResult delete(@NotNull(message = "请选择要删除的数据") Integer minus_id) {
		try {

			// 判断活动是否过期
			if (this.minusManager.isOutOfDate(minus_id)) {
				return JsonResultUtil.getErrorJson("活动尚在进行中，不可删除");
			}

			this.minusManager.delete(minus_id);
			return JsonResultUtil.getSuccessJson("删除成功！");
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			return JsonResultUtil.getErrorJson("删除失败！");
		}
	}

	/**
	 * 按id获取单品立减活动
	 * 
	 * @param MinusId
	 *            单品立减活动Id
	 * @return MinusVo 单品立减活动对象
	 */
	@ResponseBody
	@ApiImplicitParam(name = "minusId", value = "单品立减对象", required = true, dataType = "Integer", paramType = "path")
	@GetMapping("/get")
	public MinusVo get(Integer MinusId, Integer shop_id) {
		return this.minusManager.get(MinusId);
	}

}
