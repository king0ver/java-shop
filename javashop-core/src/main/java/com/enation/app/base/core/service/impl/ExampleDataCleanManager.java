package com.enation.app.base.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.service.IExampleDataCleanManager;

import com.enation.framework.cache.ICache;
import com.enation.framework.database.IDaoSupport;

/**
 * 
 * 示例数据清除管理 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月19日 下午4:39:02
 */
@Service("exampleDataCleanManager")
public class ExampleDataCleanManager implements IExampleDataCleanManager {

	@Autowired
	private IDaoSupport daoSupport;
	@Autowired
	private ICache cache;
	@Override
	public void clean() {
		String sql = "TRUNCATE table es_goods";// 商品
		this.daoSupport.execute(sql);
		cache.remove("all_category");//分类缓存
		sql = "TRUNCATE table es_goods_category";//商品分类
		this.daoSupport.execute(sql);
		cache.remove("all_category");//分类缓存
		sql = "TRUNCATE table es_goods_gallery";//商品相册
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_goods_sku";// sku
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_store_log";
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_tag_rel";// 店铺标签关联表
		this.daoSupport.execute(sql);
		sql = "update es_member set disabled=0,store_id=0,is_store=0 where store_id!=?";// 删除会员关联的店铺
		this.daoSupport.execute(sql, 1);
		sql = "delete from es_shop_detail where shop_id!=?";// 店铺详情
		this.daoSupport.execute(sql, 1);
		sql = "delete from es_shop where shop_id!=?";// 店铺
		this.daoSupport.execute(sql, 1);
		sql = "delete from es_store_silde where store_id!=?";
		this.daoSupport.execute(sql, 1);
		sql = "update es_member set disabled=1";// 会员表
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_brand";// 品牌
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_category_brand";// 分类关联品牌
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_category_spec";// 分类关联规格
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_exchange_setting";// 积分换购信息
		this.daoSupport.execute(sql);
		sql = "TRUNCATE table es_goods_params";// 商品参数
		this.daoSupport.execute(sql);
	}

}
