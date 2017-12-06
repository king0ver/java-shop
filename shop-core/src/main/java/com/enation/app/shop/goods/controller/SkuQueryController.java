package com.enation.app.shop.goods.controller;

import java.util.List;

import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.GoodsSku;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.service.IGoodsSkuManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.annotation.PageNo;
import com.enation.framework.annotation.PageSize;
import com.enation.framework.database.Page;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Api(description = "查询商品sku")
@RestController
@RequestMapping("/shop")
public class SkuQueryController {

	@Autowired
	private ISellerManager sellerMangager;

	@Autowired
	private IGoodsSkuManager goodsSkuManager;

	@Autowired
	private IGoodsManager goodsManager;

	@ApiOperation(value = "卖家查询sku列表")
	@GetMapping(value = "/seller/sku/search")
	public Page sellerQuery(@ApiIgnore @PageNo Integer page_no, @ApiIgnore @PageSize Integer page_size,
			GoodsQueryParam goodsQueryParam) {

		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}

		goodsQueryParam.setSeller_id(seller.getStore_id());
		if (goodsQueryParam.getStype() == null) {
			goodsQueryParam.setStype(0);
		}

		goodsQueryParam.setPage_no(page_no);
		goodsQueryParam.setPage_size(page_size);
		Page page = goodsSkuManager.query(goodsQueryParam);
		return page;
	}

	@ApiOperation(value = "管理员查询sku列表")
	@GetMapping(value = "/admin/sku/search")
	public Page adminQuery(@ApiIgnore @PageNo Integer page_no, @ApiIgnore @PageSize Integer page_size,
			GoodsQueryParam goodsQueryParam) {

		AdminUser admin = UserConext.getCurrentAdminUser();
		if (admin == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		if (goodsQueryParam.getStype() == null) {
			goodsQueryParam.setStype(0);
		}
		goodsQueryParam.setPage_no(page_no);
		goodsQueryParam.setPage_size(page_size);
		Page page = goodsSkuManager.query(goodsQueryParam);
		return page;
	}

	@ApiOperation(value = "查询sku集合")
	@GetMapping(value = "/skulist/{skuidAr}")
	public List<GoodsSku> queryByIdAr(@PathVariable(name = "skuidAr") Integer[] skuidAr) {

		return this.goodsSkuManager.query(skuidAr);
	}

	/**
	 * 商家中心编辑商品时展示sku信息
	 *
	 * @param goods_id
	 *            商品id
	 * @return
	 */
	@ApiOperation(value = "商品sku信息信息获取api")
	@GetMapping(value = "/goods/{goods_id}/skus")
	public List<GoodsSkuVo> queryByGoodsId(@PathVariable(name = "goods_id") Integer goods_id) {

		GoodsVo goods = goodsManager.getFromCache(goods_id);
		return  goods.getSkuList();
	}
}
