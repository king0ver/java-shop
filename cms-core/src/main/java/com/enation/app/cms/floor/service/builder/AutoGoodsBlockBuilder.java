package com.enation.app.cms.floor.service.builder;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.cms.floor.model.vo.AutoGoodsBlock;
import com.enation.app.cms.floor.model.vo.AutoRule;
import com.enation.app.cms.floor.model.vo.Goods;
import com.enation.app.cms.floor.service.Element;
import com.enation.eop.sdk.config.JavashopConfig;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.StringUtil;

/**
 * 自动规则商品构建
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月12日 下午2:00:22
 */
@Service
public class AutoGoodsBlockBuilder implements Builder {

	@Autowired
	private JavashopConfig javashopConfig;
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public Element build(Map map) {
		AutoGoodsBlock goodsBlock = new AutoGoodsBlock();
		goodsBlock.buildSelf(map);
		//商品选择器选完商品要根据商品id查出商品信息展示
		Map ruleMap = (Map) map.get("rule");
		AutoRule rule = new AutoRule();
		Integer category_id1 = StringUtil.toInt(ruleMap.get("category_id1")+"", 0);
		Integer category_id2 = StringUtil.toInt(ruleMap.get("category_id2")+"", 0);
		Integer category_id3 = StringUtil.toInt(ruleMap.get("category_id3")+"", 0);
		String sort = ruleMap.get("sort")+"";
		String order = ruleMap.get("order")+"";
		Integer number = StringUtil.toInt(ruleMap.get("number")+"", 0);
		rule.setCategory_id1(category_id1);
		rule.setCategory_id2(category_id2);
		rule.setCategory_id3(category_id3);
		rule.setCategory_name(ruleMap.get("category_name")+"");
		rule.setOrder(order);
		rule.setSort(sort);
		rule.setNumber(number);
		
		goodsBlock.setRule(rule);
		
		String sql = "select goods_id, goods_name, thumbnail ,price goods_price from es_goods where category_id =? or category_id =? or category_id =? "
				+ " order by "+order+" "+sort+" limit 0,?";
		
		List<Goods> list = daoSupport.queryForList(sql, Goods.class, category_id1,category_id2,category_id3,number);

		if (list != null && list.size()>0) {
			for(Goods goods : list){
				goods.setGoods_url(javashopConfig.getGoodsdetail_url()+"/goods-"+goods.getGoods_id()+".html");
			}
			goodsBlock.setGoodsList(list);
		}
		return goodsBlock;
	}

}
