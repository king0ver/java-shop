/**
 * 
 */
package com.enation.app.shop.goodssearch.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enation.app.shop.goodssearch.model.GoodsWords;
import com.enation.app.shop.goodssearch.service.IGoodsSearchManager;
import com.enation.app.shop.goodssearch.service.SearchEngineFactory;
import com.enation.framework.action.JsonResult;
import com.enation.framework.util.JsonResultUtil;

/**
 * 商品分词搜索
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月15日 下午5:48:50
 */
@RestController
@RequestMapping("/goods-search")
public class GoodsWordsApiController  {
	

	@GetMapping("/shop/goods-words")
	public JsonResult listWords(String keyword){
		try{
			
			IGoodsSearchManager goodsSearchManager = SearchEngineFactory.getSearchEngine();
			
			List<GoodsWords> wordsList = goodsSearchManager.getGoodsWords(keyword);
			
			return JsonResultUtil.getObjectJson(wordsList);
		}catch(Exception e){
			e.printStackTrace();
			return JsonResultUtil.getErrorJson("error");
		}
	}
}
