package com.enation.app.shop.goods.controller;



import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.app.shop.goods.model.vo.GoodsSkuVo;
import com.enation.app.shop.goods.model.vo.GoodsVo;
import com.enation.app.shop.goods.service.IGoodsManager;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 商品维护 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 上午11:24:51
 */
@Api(description = "商家中心商品api，相关写操作")
@RestController
@RequestMapping("/goods/seller/goods")
@Validated
public class GoodsOperatorController {

	@Autowired
	private IGoodsManager goodsManager;
	@Autowired
	private ISellerManager sellerMangager;
	/**
	 * 商家添加上架商品
	 * @param goodsVo 商品的所有信息，包括参数和sku,相册等
	 * @throws Exception
	 */
	@ApiOperation(value = "商家添加上架商品接口",notes = "上架商品时使用")
	@RequestMapping(method = RequestMethod.POST)
	public void add(@Valid @RequestBody GoodsVo goodsVo) throws Exception{
		Seller seller = sellerMangager.getSeller();
		if(seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		goodsManager.add(goodsVo);
	}
	/**
	 * 商家修改商品
	 * @param goodsVo 商品的所有信息，包括参数和sku,相册等
	 * @throws Exception
	 */
	@ApiOperation(value = "商家修改商品接口",notes = "商家修改商品相关时使用")
	@RequestMapping(value = "/edit" ,method = RequestMethod.POST)
	public void edit(@RequestBody GoodsVo goodsVo) throws Exception{
		Seller seller = sellerMangager.getSeller();
		if(seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		Goods goods = goodsManager.getFromDB(goodsVo.getGoods_id());
		//判断当前要修改的商品是当前登陆商家的商品
		if(!(goods.getSeller_id().equals(seller.getStore_id()))) {
			throw new UnProccessableServiceException("no_login", "请您正确登陆");
		}
		
		//判断库存输入是否为负数
		if(goodsVo.getQuantity()<0){
			throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "库存不能为负数");
		}
		List<GoodsSkuVo> skuList = goodsVo.getSkuList();
		for(GoodsSkuVo vo : skuList){
			if(vo.getQuantity()<0){
				throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "库存不能为负数");
			}
		}
		
		goodsManager.edit(goodsVo);
	}
	/**
	 * 商家将商品放入回收站
	 * @param goods_ids 商品id
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "商家将商品放入回收站",notes = "商家在商品列表删除商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_ids",value="商品ID集合",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value = "/recycle" ,method = RequestMethod.POST)
	public void deleteGoods( Integer[] goods_ids) throws Exception{
		Seller seller = sellerMangager.getSeller();
		if(seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		for (int i = 0; i < goods_ids.length; i++) {
			Goods goods = goodsManager.getFromDB(goods_ids[i]);
			//判断当前要修改的商品是当前登陆商家的商品
			if(!(goods.getSeller_id().equals(seller.getStore_id()))) {
				throw new UnProccessableServiceException("no_login", "请您正确登陆");
			}
		}
		if(goods_ids!=null){
			goodsManager.inRecycle(goods_ids);
		}
	}
	/**
	 *  商家下架商品
	 * @param goods_ids
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "商家下架商品",notes = "商家下架商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_ids",value="商品ID集合",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value = "/under" ,method = RequestMethod.POST)
	public String underGoods(Integer[] goods_ids) throws Exception{
		Seller seller = sellerMangager.getSeller();
		if(seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		Integer count = goodsManager.getCountByGoodsIds(goods_ids,seller.getStore_id());
		if(count==null || !count.equals(goods_ids.length)){
			throw new UnProccessableServiceException(ErrorCode.NO_PERMISSION,"存在你没有权限操作的商品");
		}
		if(goods_ids!=null){
			goodsManager.under(goods_ids,null);
		}
		return null;
	}
	/**
	 * 商家还原商品
	 * @param goods_ids 商品ID集合
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "商家还原商品",notes = "商家回收站回收商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_ids",value="商品ID集合",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value="/revert/{goods_ids}",method = RequestMethod.POST)
	public void revertGoods(@PathVariable("goods_ids") Integer[] goods_ids) throws Exception{
		Seller seller = sellerMangager.getSeller();
		if(seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		Integer count = goodsManager.getCountByGoodsIds(goods_ids,seller.getStore_id());
		if(count==null || !count.equals(goods_ids.length)){
			throw new UnProccessableServiceException(ErrorCode.NO_PERMISSION,"存在你没有权限操作的商品");
		}
		if(goods_ids!=null){
			this.goodsManager.revert(goods_ids);
		}
	}
	/**
	 * 商家彻底删除商品
	 * @param goods_ids 商品ID集合
	 * @return 
	 * @throws Exception
	 */
	@ApiOperation(value = "商家彻底删除商品",notes = "商家回收站删除商品时使用")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_ids",value="商品ID集合",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value="/{goods_ids}",method = RequestMethod.DELETE)
	public void cleanGoods(@PathVariable("goods_ids") Integer[] goods_ids) throws Exception{
		Seller seller = sellerMangager.getSeller();
		if(seller == null || seller.getStore_id() == null) {
			throw new UnProccessableServiceException("no_login", "未登录，请稍后重试");
		}
		Integer count = goodsManager.getCountByGoodsIds(goods_ids,seller.getStore_id());
		if(count==null || !count.equals(goods_ids.length)){
			throw new UnProccessableServiceException(ErrorCode.NO_PERMISSION,"存在你没有权限操作的商品");
		}
		for (int i = 0; i < goods_ids.length; i++) {
			Goods goods = goodsManager.getFromDB(goods_ids[i]);
			//判断当前要修改的商品是当前登陆商家的商品
			if(!(goods.getSeller_id().equals(seller.getStore_id()))) {
				throw new UnProccessableServiceException("no_login", "请您正确登陆");
			}
		}
		this.goodsManager.delete(goods_ids);
	}

	/**
	 * 每次浏览商品需要记录浏览数
	 * @param goods_id
	 */
	@ApiOperation(value = "记录浏览器商品次数",notes = "记录浏览器商品次数")
	@ApiImplicitParams({
		@ApiImplicitParam(name="goods_id",value="商品ID",required=true,paramType="query",dataType="int",allowMultiple=true)
	})
	@RequestMapping(value="/visit/{goods_id}")
	public void visitGoods(@PathVariable("goods_id") Integer goods_id){
		this.goodsManager.visitedGoodsNum(goods_id);
	}

}
