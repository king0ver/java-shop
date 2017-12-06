package com.enation.app.shop.promotion.groupbuy.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuy;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 团购api
 * 
 * @author Chopper
 * @version v1.0
 * @since v6.4 2017年8月22日 下午2:51:25
 *
 */
@Controller
@RequestMapping("/api/b2b2c/groupBuy")
public class GroupBuyApiController {

	// TODO 修改api路径

	@Autowired
	private IGroupBuyManager groupBuyManager;

	@Autowired
	private ISellerManager sellerManager;
	 
	/**
	 * 添加活动
	 * 
	 * @param 团购活动
	 * @param goods_enable_store
	 *            参与活动的商品库存
	 * @param image
	 *            活动图片
	 * @return 结果
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	public JsonResult add(GroupBuy groupBuy, Integer goods_enable_store) {
		try {
			groupBuy.setGb_status(0);
			Seller storeMember = sellerManager.getSeller();
			// 判断是否登录
			if (storeMember == null) {
				throw new RuntimeException("尚未登陆，不能使用此API");
			}
			// 判断团购数量是否超过库存
			if (groupBuy.getGoods_num() > goods_enable_store) {
				return JsonResultUtil.getErrorJson("团购数量必须小于商品数量");
			} 

			groupBuy.setStore_id(storeMember.getStore_id());
			int result = groupBuyManager.add(groupBuy);
			if (result == 0) {
				Logger logger = Logger.getLogger(getClass());
				RuntimeException e = new RuntimeException("未知的异常");
				logger.error("团购添加失败", e);
				return JsonResultUtil.getErrorJson("团购添加失败" + e.getMessage());
			}
			return JsonResultUtil.getSuccessJson("团购添加成功");
		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass());
			logger.error("团购添加失败", e);
			return JsonResultUtil.getErrorJson("团购添加失败" + e.getMessage());
		}

	}

	/**
	 * 更新团购信息
	 * 
	 * @param groupBuy
	 *            团购互动
	 * @param goods_enable_store
	 *            参与商品库存
	 * @param image
	 *            图片
	 * @return 处理结果
	 */
	@ResponseBody
	@RequestMapping(value = "/update")
	public JsonResult update(GroupBuy groupBuy, Integer goods_enable_store) {
		Seller storeMember = sellerManager.getSeller();
		groupBuy.setGb_status(0);
		if (storeMember == null) {
			throw new RuntimeException("尚未登陆，不能使用此API");
		}

		// 判断团购数量是否超过库存
		if (groupBuy.getGoods_num() > goods_enable_store) {
			return JsonResultUtil.getErrorJson("团购数量必须小于商品数量");
		}
 
		try {
			groupBuyManager.update(groupBuy);
			return JsonResultUtil.getSuccessJson("团购更新成功");
		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass());
			logger.error("团购更新失败", e);
			return JsonResultUtil.getErrorJson("团购更新失败" + e.getMessage());
		}
	}

	/**
	 * 删除团购
	 * 
	 * @param gb_id
	 *            要删除团购id, int 型
	 * @return 操作结果 json字串
	 */
	@ResponseBody
	@RequestMapping(value = "/delete")
	public JsonResult delete(Integer gb_id) {
		try {
			this.groupBuyManager.delete(gb_id);
			return JsonResultUtil.getSuccessJson("删除成功");

		} catch (Exception e) {
			Logger logger = Logger.getLogger(getClass());
			logger.error("删除失败", e);
			return JsonResultUtil.getErrorJson("删除失败" + e.getMessage());
		}

	}

	/**
	 * 搜索商品 输入参数：
	 * 
	 * @param catid
	 *            ：分类id,如果不输入，则搜索全部的分类下的商品
	 * @param brandid:品牌id，如果不佃入，是搜索全部的品牌下的商品
	 * @param keyword：搜索关键字，会搜索商品名称和商品编号
	 * @return 商品搜索结果 {@link Goods}
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value = "/search")
	public Object search(String keyword, String store_catid, String act_id) {
		try {
			Seller storeMember = sellerManager.getSeller();
			if (storeMember == null) {
				return JsonResultUtil.getErrorJson("尚未登陆，不能使用此API");
			}

			Map params = new HashMap(3);
			params.put("keyword", keyword);
			params.put("store_catid", store_catid);
			params.put("act_id", act_id);
			List<Map> goodsList = this.groupBuyManager.storeGoodsList(storeMember.getStore_id(), params);
			return JsonResultUtil.getObjectJson(goodsList);

		} catch (Exception e) {
			e.printStackTrace();
			Logger logger = Logger.getLogger(getClass());
			logger.error("商品搜索出错", e);
			return JsonResultUtil.getErrorJson("api调用失败" + e.getMessage());
		}
	}
}
