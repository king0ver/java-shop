package com.enation.app.shop.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.vo.GoodsLine;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.annotation.PageNo;
import com.enation.framework.annotation.PageSize;
import com.enation.framework.database.Page;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * 商品查询
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * 2017年8月14日 下午2:24:27
 */
@Api(description = "商品查询，商品选择器使用")
@RestController
@RequestMapping("/shop")
public class GoodsQueryController {
	@Autowired
	private IGoodsQueryManager goodsQueryManager;
	@Autowired
	private ISellerManager sellerMangager;

	/**
	 * 管理员查询商品
	 * 
	 * @param page_no
	 * @param page_size
	 * @param goodsQueryParam
	 * @return
	 */
	@GetMapping(value = "/admin/goods/search")
	public Page adminQuery(@ApiIgnore @PageNo Integer page_no, @ApiIgnore @PageSize Integer page_size,
			GoodsQueryParam goodsQueryParam) {
		AdminUser user = UserConext.getCurrentAdminUser();
		// 判断是否有管理员权限
		if (user == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		if (goodsQueryParam.getStype() == null) {
			goodsQueryParam.setStype(0);
		}
		goodsQueryParam.setPage_no(page_no);
		goodsQueryParam.setPage_size(page_size);
		Page page = goodsQueryManager.query(goodsQueryParam);
		return page;
	}

	/**
	 * 卖家查询商品
	 * 
	 * @param page_no
	 * @param page_size
	 * @param goodsQueryParam
	 * @return
	 */
	@GetMapping(value = "/seller/goods/search")
	public Page sellerQuery(@ApiIgnore @PageNo Integer page_no, @ApiIgnore @PageSize Integer page_size,
			GoodsQueryParam goodsQueryParam) {
		Seller member = sellerMangager.getSeller();
		if (member == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		goodsQueryParam.setSeller_id(member.getStore_id());
		if (goodsQueryParam.getStype() == null) {
			goodsQueryParam.setStype(0);
		}
		goodsQueryParam.setPage_no(page_no);
		goodsQueryParam.setPage_size(page_size);
		Page page = goodsQueryManager.query(goodsQueryParam);
		return page;
	}
	
	@GetMapping(value = "/goodslist/{goodsidAr}")
	public List<GoodsLine> queryByIdAr( @PathVariable(name="goodsidAr")  Integer[] goodsidAr){
		
		return this.goodsQueryManager.query(goodsidAr);
	}
	
	
 
	
	
}
