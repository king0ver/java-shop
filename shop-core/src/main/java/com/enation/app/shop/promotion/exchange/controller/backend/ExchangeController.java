package com.enation.app.shop.promotion.exchange.controller.backend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.enation.app.shop.promotion.exchange.service.IExchangeManager;
import com.enation.framework.action.GridController;
import com.enation.framework.action.GridJsonResult;
import com.enation.framework.util.JsonResultUtil;

import io.swagger.annotations.Api;


/**
 * 
 * 积分商城模版编辑
 * 
 * @author zjp
 * @version v6.3.0
 * @since v6.3.1 2017年7月1日 下午5:35:46
 */
@Api(description = "积分商城模版编辑")
@Controller
@RequestMapping("/shop/admin/exchange")
public class ExchangeController extends GridController {
	@Autowired
	private IExchangeManager exchangeManager;

	/**
	 * 商品搜索
	 * 
	 * @author xulipeng 2014年5月14日16:22:13
	 * @param stype
	 *            搜索类型,Integer
	 * @param keyword
	 *            关键字,String
	 * @param name
	 *            商品名称,String
	 * @param sn
	 *            商品编号,String
	 * @param catid
	 *            商品分类Id,Integer
	 * @param page
	 *            页码,Integer
	 * @param PageSize
	 *            每页显示数量,Integer
	 * @param sort
	 *            排序,String
	 * @return 商品搜索json
	 */
	@ResponseBody
	@RequestMapping(value = "/search-goods")
	public GridJsonResult searchGoods(Integer catid, Integer stype, String keyword, String name, String sn) {
		Map goodsMap = new HashMap();
		if (stype != null) {
			if (stype == 0) {
				goodsMap.put("stype", stype);
				goodsMap.put("keyword", keyword);
			} else if (stype == 1) {
				goodsMap.put("stype", stype);
				goodsMap.put("name", name);
				goodsMap.put("sn", sn);
				goodsMap.put("catid", catid);
			}
		}
		webpage = this.exchangeManager.searchGoods(goodsMap, this.getPage(), this.getPageSize(), null, this.getSort(),
				this.getOrder());

		return JsonResultUtil.getGridJson(webpage);
	}
}
