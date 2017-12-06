package com.enation.app.shop.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.SpecValue;
import com.enation.app.shop.goods.model.po.Specification;
import com.enation.app.shop.goods.model.vo.SpecificationVo;
import com.enation.app.shop.goods.service.ISpecManager;
import com.enation.app.shop.goods.service.ISpecValueManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 规格及规格值获取API
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月25日 下午5:12:36
 */
@Api(description = "规格及规格值获取API")
@RestController
@RequestMapping("/goods-info/seller")
public class SpecFrontController {
	@Autowired
	private ISpecManager specManager;
	@Autowired
	private ISpecValueManager specValueManager;
	@Autowired
	private ISellerManager sellerMangager;

	/**
	 * 商家某分类规格的获取
	 * 
	 * @param catid
	 *            分类id
	 * @return 规格 集合
	 */
	@GetMapping("/category/{catid}/spec")
	@ApiOperation(value = "根据分类id查询规格", notes = "根据分类id查询规格")
	@ApiImplicitParam(name = "catid", value = "分类id", required = true, paramType = "path", dataType = "Integer")
	public List<Specification> sellerQuerySpec(@PathVariable Integer catid) {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		return this.specManager.querySellerSpec(catid, seller.getStore_id());

	}

	/**
	 * 商家获取某规格的规格值
	 * 
	 * @param specid
	 *            规格id
	 * @return 规格值 集合
	 */
	@GetMapping("/spec/{specid}/spec-values")
	@ApiOperation(value = "根据规格id查询规格值", notes = "根据规格id查询规格值")
	@ApiImplicitParam(name = "specid", value = "规格id", required = true, paramType = "path", dataType = "Integer")
	public List<SpecValue> sellerQueryValue(@PathVariable Integer specid) {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		return this.specValueManager.listBySpecid(specid);

	}

	/**
	 * 商家手动自定义添加规格项(商家中心规格插件使用)
	 * 
	 * @param specificationVo
	 *            规格信息
	 * @param catid
	 *            分类id
	 * @return
	 */
	@PostMapping("/category/{catid}/spec")
	@ApiOperation(value = "商家手动自定义添加规格项")
	@ApiImplicitParam(name = "catid", value = "分类id", required = true, paramType = "path", dataType = "Integer")
	public Specification saveSellerSpec(SpecificationVo specificationVo, @PathVariable Integer catid) {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		specificationVo.setCategory_id(catid);
		return this.specManager.addSellerSpec(specificationVo);

	}

	/**
	 * 商家手动自定义添加某规格项的规格值(商家中心规格插件使用)
	 * 
	 * @param specValue
	 *            规格值
	 * @param specid
	 *            规格id
	 * @return SpecValue 规格值
	 */
	@PostMapping("/spec/{specid}/spec-values")
	@ApiOperation(value = "商家手动自定义添加某规格项的规格值")
	@ApiImplicitParam(name = "specid", value = "规格id", required = true, paramType = "path", dataType = "Integer")
	public SpecValue saveSpecValue(SpecValue specValue, @PathVariable Integer specid) {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		specValue.setSpec_id(specid);
		return this.specValueManager.addSpecValue(specValue);

	}
}
