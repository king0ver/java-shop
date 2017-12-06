package com.enation.app.shop.goodssearch.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.goods.service.IGoodsQueryManager;
import com.enation.app.shop.goodssearch.service.IGoodsIndexManager;
import com.enation.app.shop.goodssearch.service.IndexTypeFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(description = "管理员生成商品的索引")
@RestController
@RequestMapping("/goods-search")
public class GoodsCreateIndexController {

	@Autowired
	private IGoodsQueryManager goodsQueryManager;
	
	@ApiOperation(value = "初始化商品索引",notes = "首次使用示例数据时使用")
	@RequestMapping(value = "/admin/goods/init", method = RequestMethod.GET)
	public void initAllIndex() throws Exception {
		
		List<Map<String, Object>> goods = this.goodsQueryManager.getGoodsAndParams(null);
		
		IGoodsIndexManager goodsIndexManager = IndexTypeFactory.getIndexType();
		
		for(Map<String, Object> map : goods){
			goodsIndexManager.deleteIndex(map);
			goodsIndexManager.addIndex(map);
		}
	}
}
