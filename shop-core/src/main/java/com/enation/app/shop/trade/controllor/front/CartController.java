package com.enation.app.shop.trade.controllor.front;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.trade.model.vo.Cart;
import com.enation.app.shop.trade.model.vo.CartVo;
import com.enation.app.shop.trade.model.vo.PriceDetail;
import com.enation.app.shop.trade.service.ICartReadManager;
import com.enation.app.shop.trade.service.ICartWriteManager;
import com.enation.app.shop.trade.service.ITradePriceManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.JsonResult;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 购物车控制器
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月18日上午9:49:07
 */
@Api(description = "购物车API")
@RestController
@RequestMapping("/api/shop/order-create")
@Validated
public class CartController {

	@Autowired
	private ICartWriteManager cartWriteManager;

	@Autowired
	private ICartReadManager cartReadManager;

	@Autowired
	private ITradePriceManager tradePriceManager;

	/**
	 * 添加一个商品
	 * 
	 * @param productid
	 * @param num
	 * @return
	 */
	@ApiOperation(value = "向购物车中添加一个产品", response = Cart.class)
	@ApiImplicitParams({
			@ApiImplicitParam(name = "skuid", value = "产品ID", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "num", value = "此产品的购买数量", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "activity_id", value = "默认参与的活动id", required = false, dataType = "int", paramType = "query") })
	@ResponseBody
	@RequestMapping(value = "/cart/product", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult add(@NotNull(message = "产品id不能为空") Integer skuid, @NotNull(message = "产品id不能为空") Integer num,
			Integer activity_id) {
		try {
			this.cartWriteManager.add(skuid, num, activity_id);
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
		return JsonResultUtil.getSuccessJson("已添加到购物车");
	}

	/**
	 * 获取购物车列表
	 * 
	 * @param showtype
	 * @return
	 */
	@ApiOperation(value = "获取购物车列表", notes = "通过showtype来控制显示方式，空或all为获取所有，checked为只获取选中的")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "showtype", value = "显示方式", required = false, dataType = "String", paramType = "query", allowableValues = "all,checked"), })
	@RequestMapping(value = "/cart", method = RequestMethod.GET)
	public List<CartVo> cartQuery(String showtype) {

		// 读取所有的购物列表
		if (StringUtil.isEmpty(showtype) || "all".equals(showtype)) {
			List<CartVo> list = this.cartReadManager.getCartlist();
			return list;
		}

		// 读取选中的列表
		if (!StringUtil.isEmpty(showtype) && "checked".equals(showtype)) {
			List<CartVo> list = this.cartReadManager.getCheckedItems();
			return list;
		}

		return new ArrayList();
	}

	@ApiOperation(value = "获取购物车中的商品数量", notes = "获取购物车中的商品数量")
	@GetMapping(value = "/count")
	public Integer count() {
		return cartReadManager.count();
	}

	/**
	 * 获取购物车列表
	 * 
	 * @param showtype
	 * @return
	 */
	@ApiOperation(value = "获取某店铺的购物车")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sellerid", value = "属主", required = true, dataType = "String", paramType = "path"), })
	@ResponseBody
	@RequestMapping(value = "/cart/seller/{sellerid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public Cart sellerQuery(@NotNull(message = "必须指定属主id") @PathVariable(name = "sellerid") Integer sellerid) {

		return this.cartReadManager.getCart(sellerid);
	}

	/**
	 * 批量删除商品
	 * 
	 * @param productids
	 * @return
	 */
	@ApiOperation(value = "删除购物车中的一个或多个产品")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "productid", value = "产品id，多个产品可以用英文逗号：(,) 隔开", required = true, dataType = "Integer[]", paramType = "path") })
	@ResponseBody
	@RequestMapping(value = "/cart/product/{productid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public JsonResult delete(@PathVariable(name = "productid") Integer[] productids) {

		try {
			this.cartWriteManager.delete(productids);
		} catch (Exception e) {
			e.printStackTrace();
			JsonResultUtil.getErrorJson("移除失败");
		}
		return JsonResultUtil.getSuccessJson("已移除购物车");

	}

	/**
	 * 清空购物车
	 * 
	 * @return
	 */
	@ApiOperation(value = "清空购物车")
	@ResponseBody
	@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
	public JsonResult clean() {
		try {
			this.cartWriteManager.clean();
			this.tradePriceManager.cleanPrice();
			return JsonResultUtil.getSuccessJson("已清空");
		} catch (Exception e) {
			if (e.getMessage() == null) {
				return JsonResultUtil.getErrorJson("出现错误，请稍后重试");
			}
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}

	/**
	 * 更新购物车中的多个产品
	 * 
	 * @param productid
	 * @param checked
	 *            1为选中，0为未选中
	 * @return
	 */
	@ApiOperation(value = "更新购物车中的多个产品", notes = "更新购物车中的多个产品的数量或选中状态")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "productIds", value = "产品id数组", required = true, dataType = "Integer[]", paramType = "path"),
			@ApiImplicitParam(name = "checked", value = "是否选中", required = false, dataType = "int", paramType = "query", allowableValues = "0,1"),
			@ApiImplicitParam(name = "num", value = "产品数量", required = false, dataType = "int", paramType = "query") })
	@ResponseBody
	@RequestMapping(value = "/cart/product/{productIds}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult update(@NotNull(message = "产品id不能为空") @PathVariable(name = "productIds") Integer[] productIds,
			@Min(value = 0) @Max(value = 1) Integer checked, Integer num) {

		try {
			if (checked != null) {
				this.cartWriteManager.checked(productIds, checked);
			}
			if (num != null) {
				this.cartWriteManager.updateNum(productIds[0], num);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("出现异常，请稍后重试");
		}
		return JsonResultUtil.getNumberJson("store", 100);

	}

	/**
	 * 设置全部商品为选中或不选中
	 * 
	 * @param productid
	 * @param checked
	 * @return
	 */
	@ApiOperation(value = "设置全部商为选中或不选中")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "checked", value = "是否选中", required = true, dataType = "int", paramType = "query", allowableValues = "0,1"), })
	@ResponseBody
	@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult updateAll(@NotNull(message = "必须指定是否选中") @Min(value = 0) @Max(value = 1) Integer checked) {

		try {
			if (checked != null) {
				this.cartWriteManager.checkedAll(checked);
				return JsonResultUtil.getSuccessJson("设置成功");
			}
			return JsonResultUtil.getErrorJson("参数有误");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}

	}

	/**
	 * 批批量设置某商家的商品为选中或不选中
	 */
	@ApiOperation(value = "批量设置某商家的商品为选中或不选中")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sellerid", value = "卖家id", required = true, dataType = "int", paramType = "path"),
			@ApiImplicitParam(name = "checked", value = "是否选中", required = true, dataType = "int", paramType = "query", allowableValues = "0,1"), })
	@ResponseBody
	@PostMapping(value = "/cart/seller/{sellerid}", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult updateSellerAll(@NotNull(message = "卖家id不能为空") @PathVariable Integer sellerid,
			@NotNull(message = "必须指定是否选中") @Min(value = 0) @Max(value = 1) Integer checked) {

		try {
			if (checked != null && sellerid != null) {
				this.cartWriteManager.checkedSellerAll(sellerid, checked);
				return JsonResultUtil.getSuccessJson("设置成功");
			}
			return JsonResultUtil.getErrorJson("参数不正确");
		} catch (Exception e) {
			return JsonResultUtil.getErrorJson(e.getMessage());
		}

	}

	/**
	 * 购物车页面读取总价
	 * @return
	 */
	@ApiOperation(value = "获取购物车总价")
	@GetMapping(value = "/cart/total")
	public JsonResult totalPrice() {
		//读取是否已经进入过结算页 只有为1是
		String is_inCheckout = (String) ThreadContextHolder.getSession().getAttribute("is_inCheckout");
		if(is_inCheckout!=null && is_inCheckout.equals("1")){
			this.cartWriteManager.refreshCart();
			ThreadContextHolder.getSession().setAttribute("is_inCheckout", "0");
		}
		PriceDetail priceDetail = tradePriceManager.getTradePrice();
		return JsonResultUtil.getObjectJson(priceDetail);
	}

	/**
	 * 商城首页右上—我的购物车
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "获取购物车总价")
	@GetMapping(value = "/cart/num-and-price")
	public JsonResult numAndPrice() {
		
		PriceDetail priceDetail = tradePriceManager.getTradePrice();
		int num = cartReadManager.count();
		Map map = new HashMap(2);
		map.put("total-num", num);
		map.put("total-price", priceDetail.getGoods_price());

		return JsonResultUtil.getObjectJson(map);
	}

	/**
	 * 设置优惠券<br>
	 * 设置优惠券的时候分为三种情况：前2种情况couponid 不为0,不为空。第3种情况couponid为0<br>
	 * 1、使用优惠券:在刚进入订单结算页，为使用任何优惠券之前。<br>
	 * 2、切换优惠券:在1、情况之后，当用户切换优惠券的时候。<br>
	 * 3、取消已使用的优惠券:用户不想使用优惠券的时候。<br>
	 * 
	 * @param sellerid
	 * @param couponid
	 * @return
	 */
	@ApiOperation(value = "设置优惠券")
	@RequestMapping(value = "/cart/{sellerid}/coupon/{couponid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult setCoupon(@NotNull(message = "卖家id不能为空") @PathVariable Integer sellerid,
			@NotNull(message = "优惠卷id不能为空") @PathVariable Integer couponid) {
		try {
			Member member = UserConext.getCurrentMember();
			if(member==null){
				return JsonResultUtil.getErrorJson("请重新登录");
			}
			this.cartWriteManager.userCoupon(couponid, sellerid);
			
			return JsonResultUtil.getSuccessJson("设置成功");
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResultUtil.getErrorJson(e.getMessage());
		}
	}

	@ApiOperation(value = "选择要参与的促销活动")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sellerid", value = "卖家id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "skuid", value = "产品id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "activity_id", value = "活动id", required = true, dataType = "int", paramType = "query"),
			@ApiImplicitParam(name = "promotion_type", value = "活动类型", required = true, dataType = "String", paramType = "query"), })
	@RequestMapping(value = "/cart/promotion", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public JsonResult setPromotion(Integer sellerid, Integer skuid, Integer activity_id, String promotion_type) {

		this.cartWriteManager.setPromotion(sellerid, skuid, activity_id, promotion_type);
		return JsonResultUtil.getSuccessJson("设置成功");
	}
	

	public CartController() {

	}

	public void finalize() throws Throwable {

	}
}