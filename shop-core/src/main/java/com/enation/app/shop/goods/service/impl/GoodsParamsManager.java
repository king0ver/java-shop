package com.enation.app.shop.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goods.service.IGoodsParamsManager;
import com.enation.framework.database.IDaoSupport;


/**
 * 
 * 商品参数manager 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月7日 下午2:07:24
 */
@Service
public class GoodsParamsManager implements IGoodsParamsManager{

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addParams(List<GoodsParams> paramList,Integer goods_id) {
		
		if(paramList!=null){
			String sql = "delete from es_goods_params where goods_id = ?";
			this.daoSupport.execute(sql, goods_id);
			for(GoodsParams param : paramList){
				param.setGoods_id(goods_id);
				this.daoSupport.insert("es_goods_params", param);//参数
			}
		}
	}
}
