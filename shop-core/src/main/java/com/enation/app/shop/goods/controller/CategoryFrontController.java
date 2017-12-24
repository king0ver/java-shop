package com.enation.app.shop.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.service.ICategoryManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 前台获取分类接口
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月14日 下午12:17:38
 */
@RestController
@RequestMapping("/goods-info")
public class CategoryFrontController {
	@Autowired
	private ICategoryManager categoryManager;
	@Autowired
	private ISellerManager sellerMangager;

	/**
	 * 商品发布，获取当前登录用户选择经营类目的所有父
	 * 
	 * @param cat_id  分类id
	 * @return
	 */
	@ApiOperation(value = "商品发布，获取当前登录用户选择经营类目的所有父")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "category_id", value = "分类id，顶级为0", required = true, dataType = "int", paramType = "query"), })
	@RequestMapping(value = "/seller/category-child", method = RequestMethod.GET)
	public List<Category> list(Integer cat_id) {
		Seller seller = sellerMangager.getSeller();
		if (seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		List<Category> list = this.categoryManager.getGoodsParentsType(cat_id);

		return list;
	}

}
