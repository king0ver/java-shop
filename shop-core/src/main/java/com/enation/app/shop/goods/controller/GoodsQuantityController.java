package com.enation.app.shop.goods.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.vo.GoodsQuantityVo;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.goods.service.IGoodsQuantityManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "商家中心商品库存单独维护api")
@RestController
@RequestMapping("/goods/seller/goods/quantity")
@Validated
public class GoodsQuantityController {

	@Autowired
	private IGoodsQuantityManager goodsQuantityManager;
	@Autowired
	private ISellerManager sellerMangager;
	@Autowired
	private IGoodsManager goodsManager;

	@ApiOperation(value = "商家单独维护库存接口", notes = "商家单独维护库存接口时使用")
	@PostMapping
	public void updateQuantity(@Valid @RequestBody GoodsVo goodsVo) throws Exception {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		GoodsVo goods = goodsManager.getFromCache(goodsVo.getSkuList().get(0).getGoods_id());
		// 判断当前要修改的商品是当前登陆商家的商品
		if (!(goods.getSeller_id().equals(seller.getStore_id()))) {
			throw new UnProccessableServiceException("no_login", "请您正确登陆");
		}
		if (goodsVo.getSkuList() == null || goodsVo.getSkuList().size() == 0) {
			throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "参数错误");
		}
		List<GoodsQuantityVo> list = new ArrayList<>();
		for (GoodsSkuVo skuvo : goodsVo.getSkuList()) {
			GoodsQuantityVo goodsQuantityVo = new GoodsQuantityVo();
			if (skuvo.getQuantity() < 0 || skuvo.getQuantity() == null) {
				throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "货品总库存不能为空或负数");
			}

			if (skuvo.getQuantity()<skuvo.getEnable_quantity()) {
				throw new UnProccessableServiceException(ErrorCode.INVALID_REQUEST_PARAMETER, "货品库存数不能小于发货数");
			}
			goodsQuantityVo.setQuantity(skuvo.getQuantity());
			goodsQuantityVo.setGoods_id(skuvo.getGoods_id());
			goodsQuantityVo.setEnable_quantity(skuvo.getQuantity()-skuvo.getEnable_quantity());
			goodsQuantityVo.setSku_id(skuvo.getSku_id());
			list.add(goodsQuantityVo);
		}
		this.goodsQuantityManager.updateGoodsQuantity(list);
	}

}
