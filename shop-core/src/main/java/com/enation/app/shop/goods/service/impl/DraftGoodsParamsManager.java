package com.enation.app.shop.goods.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.goods.model.po.DraftGoodsParams;
import com.enation.app.shop.goods.model.po.GoodsParams;
import com.enation.app.shop.goods.service.IDraftGoodsParamsManager;
import com.enation.framework.database.IDaoSupport;
/**
 * 
 * 草稿箱参数 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月30日 上午9:57:16
 */
@Service
public class DraftGoodsParamsManager implements IDraftGoodsParamsManager{
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void addParams(List<GoodsParams> paramList, Integer goods_id) {
		if(paramList!=null){
			String sql = "delete from es_draft_goods_params where draft_goods_id = ?";
			this.daoSupport.execute(sql, goods_id);
			for (GoodsParams param : paramList){
				DraftGoodsParams draftGoodsParams = new DraftGoodsParams(param);
				draftGoodsParams.setDraft_goods_id(goods_id);
				this.daoSupport.insert("es_draft_goods_params", draftGoodsParams);//参数
			}
		}
		
	}
}
