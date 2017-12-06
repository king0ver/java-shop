package com.enation.app.shop.goods.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.DraftGoods;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IDraftGoodsManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 商家中心 草稿箱商品操作
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月27日 下午1:13:35
 */
@Api(description = "商家中心 草稿箱商品操作")
@RestController
@RequestMapping("/shop/seller/draft_goods")
public class DraftGoodsController {
	@Autowired
	private ISellerManager sellerMangager;
	@Autowired
	private IDraftGoodsManager draftGoodsManager;

	/**
	 * 商品添加至草稿箱
	 * 
	 * @param goodsVo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "商家添加草稿箱商品接口", notes = "商家添加草稿箱商品时使用")
	@PostMapping(value = "/add")
	public DraftGoods add(@Valid @RequestBody GoodsVo goodsVo) throws Exception {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		DraftGoods draftGoods = this.draftGoodsManager.add(goodsVo);
		return draftGoods;
	}

	@ApiOperation(value = "商家修改草稿箱商品接口", notes = "商家修改草稿箱商品时使用")
	@PostMapping(value = "/edit")
	public DraftGoods edit(@Valid @RequestBody GoodsVo goodsVo) throws Exception {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		DraftGoods draftGoods = this.draftGoodsManager.edit(goodsVo);
		return draftGoods;
	}

	@ApiOperation(value = "草稿箱商品上架接口", notes = "草稿箱商品上架时使用")
	@PostMapping(value = "/add-marcket")
	public DraftGoods addMarcket(@Valid @RequestBody GoodsVo goodsVo) throws Exception {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		draftGoodsManager.addMarcket(goodsVo);
		return null;
	}

	@ApiOperation(value = "商家删除草稿箱商品接口", notes = "商家删除草稿箱商品时使用")
	@DeleteMapping(value = "/{goods_ids}")
	public GoodsVo delete(@PathVariable("goods_ids") Integer[] goods_ids) throws Exception {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		draftGoodsManager.delete(goods_ids);
		return null;
	}

	@ApiOperation(value = "查询草稿箱商品sku信息", notes = "商家中心编辑草稿箱商品时获得sku信息")
	@GetMapping(value = "/{goods_ids}")
	public List<GoodsSkuVo> queryByDraftGoodsid(@PathVariable("goods_ids") Integer goods_ids) throws Exception {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		List<GoodsSkuVo> list = draftGoodsManager.draftGoodsSkuList(goods_ids);
		return list;
	}

}
