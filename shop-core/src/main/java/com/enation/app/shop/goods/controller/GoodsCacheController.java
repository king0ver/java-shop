package com.enation.app.shop.goods.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.goods.model.vo.CacheSku;
import com.enation.app.shop.goods.model.vo.GoodsCache;
import com.enation.app.shop.goods.service.IGoodsQueryManager;

import io.swagger.annotations.Api;

/**
 * 商品缓存使用controller
 * @author fk
 * @version v1.0
 * 2017年6月8日 上午10:56:06
 */
@Api(description = "商品缓存使用controller")
@RestController
@RequestMapping("/goods/admin/goods")
public class GoodsCacheController {

	@Autowired
	private RedisTemplate<String,Object> redisTempale;
	
	@Autowired
	private IGoodsQueryManager goodsQueryManager;
	
	@PostMapping("/init-cache")
	public boolean initCache(){
		List<GoodsCache> list = goodsQueryManager.queryAllGoodsForCache();
		for(GoodsCache map :list){
			Integer goods_id = map.getGoodsid();
			List<CacheSku> skuList = (List<CacheSku>) map.getSkuList();
			redisTempale.opsForValue().set("store_"+goods_id, skuList);
		}
		return true;
	}
	
}
