package com.enation.app.shop.promotion.exchange.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.promotion.exchange.service.IExchangeFloorManager;
import com.enation.framework.database.IDaoSupport;
/**
 * 
 * 积分商品楼层管理实现类
 * @author    jianghongyan
 * @version   1.0.0,2016年6月20日
 * @since     v6.1
 * @deprecated 已过时
 */
@Deprecated
@Service
public class ExchangeFloorManager implements IExchangeFloorManager{
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public List listAll() {
		// TODO Auto-generated method stub
		String sql="SELECT * FROM es_exchange_floor ORDER BY floor";
		return daoSupport.queryForList(sql);
	}

	@Override
	public Map getExchangeGoodsByType(Integer type) {
		String sql="SELECT eg.*,fr.attr,ef.args,es.* FROM es_goods eg "
					+"LEFT JOIN es_floor_rel fr ON eg.goods_id=fr.goods_id "
					+"LEFT JOIN es_exchange_floor ef ON ef.type=fr.floor_type_id "
					+"LEFT JOIN es_exchange_setting es ON es.goods_id=eg.goods_id "
					+"where ef.type=?";
		List<Map<String,Object>> exchangeGoodsList= daoSupport.queryForList(sql,type);
		Map<String,List<Map<String,Object>>> map=new HashMap<String, List<Map<String,Object>>>();
		for (Map<String,Object> exchangeGoods : exchangeGoodsList) {
			String attr=exchangeGoods.get("attr").toString().trim();
			String key=exchangeGoods.get("key").toString().trim();
			JSONObject jo=JSONObject.fromObject(attr);
			String atkey=jo.get(key).toString();
			if(map.containsKey(atkey)){
				map.get(atkey).add(exchangeGoods);
			}else{
				List l= new ArrayList<Map<String,Object>>();
				l.add(exchangeGoods);
				map.put(atkey,l);
			}
		}
		return map;
	}

}
