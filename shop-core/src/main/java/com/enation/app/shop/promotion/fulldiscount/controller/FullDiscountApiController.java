package com.enation.app.shop.promotion.fulldiscount.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 
 * 满优惠促销控制器
 * @author zjp
 * @version v1.0
 * @since v6.4.0
 * 2017年9月4日 下午5:01:04
 */

import com.ctc.wstx.util.DataUtil;
import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.ShopApp;
import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.promotion.fulldiscount.model.vo.FullDiscountVo;
import com.enation.app.shop.promotion.fulldiscount.service.IFullDiscountManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.model.vo.PromotionGoodsVo;
import com.enation.app.shop.promotion.tool.support.PromotionServiceConstant;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.cache.ICache;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonResultUtil;
import com.enation.framework.util.StringUtil;

@RestController
@Scope("prototype")
@RequestMapping("/api/shop/promotion/filldiscount")
public class FullDiscountApiController {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private ISellerManager sellerManager;

	@Autowired
	private IFullDiscountManager fullDiscountManager;
	@Autowired
	private IGoodsQueryManager goodsQueryManager;
	@Autowired
	private ICache cache;

	@PostMapping(value = "/saveAdd", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRED)
	public JsonResult add(@RequestBody FullDiscountVo fullDiscountVo) {
		Integer self = ShopApp.self_storeid;
		Seller member = this.sellerManager.getSeller();
		if (member == null) {
			return JsonResultUtil.getErrorJson("未登录，不能使用此api");
		}
		if (self != member.getStore_id() && fullDiscountVo.getPoint_value() != null) {
			return JsonResultUtil.getErrorJson("非自营店铺不得添加送积分活动");
		}
		/** 校验活动信息 */
		JsonResult checkInfo = checkInfo(fullDiscountVo);
		if (checkInfo != null) {
			return checkInfo;
		}
		FullDiscountVo addInfo = addInfo(fullDiscountVo);

		// 判断促销商品在同一时间段是否参加了其他的重复的促销活动,返回没有参加活动的商品ID和活动ID
		if (fullDiscountVo != null) {
			// TODO 商品选择器添加规格以后需要在验证这做修改
			if (this.fullDiscountManager.checkGoods(addInfo)) {
				JsonResult jsonRetult = new JsonResult();
				jsonRetult.setResult(0);
				return jsonRetult;
			}
		}

		fullDiscountManager.add(fullDiscountVo);
		return JsonResultUtil.getSuccessJson("添加成功");
	}

	@PostMapping(value = "/saveEdit", produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(propagation = Propagation.REQUIRED)
	public JsonResult saveEdit(@RequestBody FullDiscountVo fullDiscountVo) {
		try {
			Integer self = ShopApp.self_storeid;
			Seller member = this.sellerManager.getSeller();
			if (member == null) {
				return JsonResultUtil.getErrorJson("未登录，不能使用此api");
			}
			if (self != member.getStore_id() && fullDiscountVo.getPoint_value() != null) {
				return JsonResultUtil.getErrorJson("非自营店铺不得添加送积分活动");
			}
			/** 如果活动已过期则不进行修改操作 */
			String plugin = PromotionServiceConstant.getFullDiscountKey(
					fullDiscountVo.getFd_id());
			FullDiscountVo fullDiscount = (FullDiscountVo) cache.get(plugin);
			if (fullDiscount.getStart_time() < DateUtil.getDateline()
					&& fullDiscount.getEnd_time() > DateUtil.getDateline()) {
				return JsonResultUtil.getErrorJson("活动正在进行中，无法修改促销信息！");
			}
			if (fullDiscount.getEnd_time() < DateUtil.getDateline()) {
				return JsonResultUtil.getErrorJson("活动已截止");
			}
			/** 校验活动信息 */
			JsonResult checkInfo = checkInfo(fullDiscountVo);
			if (checkInfo != null) {
				return checkInfo;
			}

			FullDiscountVo addInfo = addInfo(fullDiscountVo);

			// 判断促销商品在同一时间段是否参加了其他的重复的促销活动,返回没有参加活动的商品ID和活动ID
			if (fullDiscountVo != null) {
				// TODO 商品选择器添加规格以后需要在验证这做修改
				if (this.fullDiscountManager.checkGoods(addInfo)) {
					JsonResult jsonRetult = new JsonResult();
					jsonRetult.setResult(0);
					return jsonRetult;
				}
			}

			this.fullDiscountManager.edit(fullDiscountVo);
			return JsonResultUtil.getSuccessJson("修改促销活动成功！");
		} catch (Exception e) {
			this.logger.error("修改促销活动失败:", e);
			return JsonResultUtil.getErrorJson("修改促销活动失败！");
		}

	}

	/**
	 * 删除促销活动
	 * 
	 * @param activity_id
	 *            促销活动id
	 * @return 返回删除的结果
	 */
	@DeleteMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonResult delete(Integer fd_id) {
		try {

			/** 获取当前登陆的店铺ID */
			Seller seller = sellerManager.getSeller();
			FullDiscountVo fullDiscountVo = this.fullDiscountManager.get(fd_id);
			if (fullDiscountVo != null) {
				/** 增加权限 */
				if (fullDiscountVo.getShop_id() == null || !fullDiscountVo.getShop_id().equals(seller.getStore_id())) {
					return JsonResultUtil.getErrorJson("您没有权限");
				}
			}
			/** 进行中的活动无法 */
			if (fullDiscountVo.getStart_time() < DateUtil.getDateline()
					&& fullDiscountVo.getEnd_time() > DateUtil.getDateline()) {
				return JsonResultUtil.getErrorJson("活动正在进行中，无法删除促销！");
			}
			/** 如果促销活动id等于空 */
			if (fd_id == null) {
				return JsonResultUtil.getErrorJson("请选择要删除的数据");
			}

			this.fullDiscountManager.delete(fd_id);
			return JsonResultUtil.getSuccessJson("删除成功！");
		} catch (Exception e) {
			this.logger.error("删除失败：", e);
			return JsonResultUtil.getErrorJson("删除失败！");
		}
	}

	/**
	 * 校验活动信息
	 * 
	 * @param fullDiscountVo
	 *            活动vo
	 * @return
	 */
	private JsonResult checkInfo(FullDiscountVo fullDiscountVo) {

		Seller seller = this.sellerManager.getSeller();

		Long start_time = DateUtil.getDateline(fullDiscountVo.getStart_time_str(), "yyyy-MM-dd HH:mm:ss");
		Long end_time = DateUtil.getDateline(fullDiscountVo.getEnd_time_str(), "yyyy-MM-dd HH:mm:ss");

		/** 向表填充数据用于保存活动表 */
		fullDiscountVo.setStart_time(start_time);
		fullDiscountVo.setEnd_time(end_time);
		fullDiscountVo.setDisabled(0);
		fullDiscountVo.setShop_id(seller.getStore_id());
		/** 促销活动名称不能为空 */
		if (StringUtil.isEmpty(fullDiscountVo.getTitle())) {
			return JsonResultUtil.getErrorJson("请填写促销活动名称");
		}
		// 促销活动的生效日期不能为空
		if (StringUtil.isEmpty(fullDiscountVo.getStart_time_str())
				|| StringUtil.isEmpty(fullDiscountVo.getEnd_time_str())) {
			return JsonResultUtil.getErrorJson("请填写促销活动生效时间");
		}

		if (fullDiscountVo.getEnd_time() < fullDiscountVo.getStart_time()) {
			return JsonResultUtil.getErrorJson("开始时间不能大于结束时间");
		}

		// 促销活动的优惠门槛不能为空也不能为0
		if (fullDiscountVo.getFull_money() == null || fullDiscountVo.getFull_money() == 0) {
			return JsonResultUtil.getErrorJson("优惠门槛不能为空或不能为0");
		}

		// 促销活动的优惠方式不能全部为空，至少要选择一项
		if (fullDiscountVo.getIs_full_minus() == null && fullDiscountVo.getIs_send_point() == null
				&& fullDiscountVo.getIs_free_ship() == null && fullDiscountVo.getIs_send_gift() == null
				&& fullDiscountVo.getIs_send_bonus() == null && fullDiscountVo.getIs_discount() == null) {
			return JsonResultUtil.getErrorJson("请选择优惠方式");
		}

		// 如果促销活动优惠详细是否包含满减不为空
		if (fullDiscountVo.getIs_full_minus() != null && fullDiscountVo.getIs_full_minus() == 1) {

			if (fullDiscountVo.getMinus_value() == null || fullDiscountVo.getMinus_value() == 0) {
				return JsonResultUtil.getErrorJson("减少的现金不能为空或不能为0");
			}

			// 减少的现金必须小于优惠门槛
			if (fullDiscountVo.getMinus_value() > fullDiscountVo.getFull_money()) {
				return JsonResultUtil.getErrorJson("减少的现金不能多于" + fullDiscountVo.getFull_money() + "元");
			}

		}

		// 如果促销活动优惠详细是否包含满送积分不为空
		if (fullDiscountVo.getIs_send_point() != null && fullDiscountVo.getIs_send_point() == 1) {
			// 赠送的积分不能为空也不能为0
			if (fullDiscountVo.getPoint_value() == null || fullDiscountVo.getPoint_value() == 0) {
				return JsonResultUtil.getErrorJson("赠送的积分不能为空或不能为0");
			}
		}

		// 如果促销活动优惠详细是否包含满送赠品不为空
		if (fullDiscountVo.getIs_send_gift() != null && fullDiscountVo.getIs_send_gift() == 1) {
			// 赠品id不能为0
			if (fullDiscountVo.getGift_id() == 0) {
				return JsonResultUtil.getErrorJson("请选择赠品");
			}
		}

		// 如果促销活动优惠详细是否包含满送优惠券不为空
		if (fullDiscountVo.getIs_send_bonus() != null && fullDiscountVo.getIs_send_bonus() == 1) {
			// 优惠券id不能为0
			if (fullDiscountVo.getBonus_id() == 0) {
				return JsonResultUtil.getErrorJson("请选择优惠券");
			}
		}

		// 如果促销活动优惠详细是否包含打折不为空
		if (fullDiscountVo.getIs_discount() != null && fullDiscountVo.getIs_discount() == 1) {
			// 打折的数值不能为空也不能为0
			if (fullDiscountVo.getDiscount_value() == null || fullDiscountVo.getDiscount_value() == 0) {
				return JsonResultUtil.getErrorJson("打折的数值不能为空或不能为0");
			}
			// 打折的数值应介于0-10之间
			if (fullDiscountVo.getDiscount_value() >= 10 || fullDiscountVo.getDiscount_value() <= 0) {
				return JsonResultUtil.getErrorJson("打折的数值应介于0到10之间");
			}
		}

		/** 如果促销活动选择的是部分商品参加活动 */
		if (fullDiscountVo.getRange_type() == 2 && fullDiscountVo.getGoodsList().size() == 0) {
			return JsonResultUtil.getErrorJson("请选择要参与活动的商品");
		}
		return null;
	}

	/**
	 * 填充活动商品信息
	 * 
	 * @param fullDiscountVo
	 * @return
	 */
	private FullDiscountVo addInfo(FullDiscountVo fullDiscountVo) {
		List<PromotionGoodsVo> promotionGoodsVoList = new ArrayList<PromotionGoodsVo>();
		/** 判断是全部商品参数还是部分商品参与 */
		if (fullDiscountVo.getRange_type() == 1) {
			/** 所有商品参与需要查询店铺的所有有效商品 */
			List<Map> goodsList = goodsQueryManager.getGoodsList(fullDiscountVo.getShop_id());
			for (Map map : goodsList) {
				PromotionGoodsVo promotionGoodsVo = new PromotionGoodsVo();
				promotionGoodsVo.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
				promotionGoodsVo.setEnd_time(fullDiscountVo.getEnd_time());
				promotionGoodsVo.setStart_time(fullDiscountVo.getStart_time());
				promotionGoodsVo.setTitle(fullDiscountVo.getTitle());
				promotionGoodsVo.setGoods_id(Integer.parseInt(map.get("goods_id").toString()));
				promotionGoodsVo.setProduct_id(Integer.parseInt(map.get("sku_id").toString()));
				promotionGoodsVo.setThumbnail(StringUtil.toString(map.get("thumbnail")));
				promotionGoodsVo.setName(map.get("goods_name").toString());
				promotionGoodsVoList.add(promotionGoodsVo);
				fullDiscountVo.setGoodsList(promotionGoodsVoList);
			}
		} else {
			/** 部分商品填充商品信息 */
			promotionGoodsVoList = fullDiscountVo.getGoodsList();
			for (PromotionGoodsVo promotionGoodsVo : promotionGoodsVoList) {
				promotionGoodsVo.setPromotion_type(PromotionTypeEnum.FULLDISCOUNT.getType());
				promotionGoodsVo.setEnd_time(fullDiscountVo.getEnd_time());
				promotionGoodsVo.setStart_time(fullDiscountVo.getStart_time());
				promotionGoodsVo.setTitle(fullDiscountVo.getTitle());
			}
		}

		return fullDiscountVo;
	}
}
