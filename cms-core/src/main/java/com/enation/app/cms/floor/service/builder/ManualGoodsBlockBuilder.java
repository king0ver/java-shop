package com.enation.app.cms.floor.service.builder;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.vo.Goods;
import com.enation.app.cms.floor.model.vo.ManualGoodsBlock;
import com.enation.app.cms.floor.service.Element;
import com.enation.eop.sdk.config.JavashopConfig;
import com.enation.framework.database.IDaoSupport;

/**
 * 
 * 手动规则商品
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月13日 下午9:38:16
 */
@Service
public class ManualGoodsBlockBuilder implements Builder {

	@Autowired
	private JavashopConfig javashopConfig;
	@Autowired
	private IDaoSupport daoSupport;
	@Override
	public Element build(Map map) {
		ManualGoodsBlock goodsBlock = new ManualGoodsBlock();
		goodsBlock.buildSelf(map);
		//商品选择器选完商品要根据商品id查出商品信息展示
		Integer goodsid =Double.valueOf(""+map.get("goods_id")).intValue();
		goodsBlock.setGoods_id(goodsid);
		String sql = "select goods_id, goods_name, thumbnail ,price goods_price from es_goods where goods_id = ? and disabled=0 ";
		Goods goodsvo = daoSupport.queryForObject(sql, Goods.class, goodsid);

		if (goodsvo != null) {
		
			goodsvo.setGoods_url(javashopConfig.getGoodsdetail_url()+"/goods-"+goodsid+".html");
			goodsBlock.setGoods(goodsvo);
			
		}
		return goodsBlock;
	}

}
