package com.enation.app.shop.goods.service.impl;

import com.enation.app.base.AmqpExchange;
import com.enation.framework.jms.support.goods.GoodsReasonMsg;
import com.enation.framework.util.StringUtil;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.po.Category;
import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.goods.service.IGoodsAuthManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

/**
 * 
 * 商品审核业务实现
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年9月29日 下午5:39:12
 */
@Service
public class GoodsAuthManager implements IGoodsAuthManager {
	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private AmqpTemplate amqpTemplate;

	@Override
	public Page getList(GoodsQueryParam goodsQueryParam) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select g.*,b.name as brand_name ,c.name from es_goods  g left join es_goods_category c on g.category_id=c.category_id left join es_brand b on g.brand_id = b.brand_id and b.disabled=0 where is_auth=0 and market_enable=1 ");
		if (goodsQueryParam.getStype().intValue() == 0) {
			if (!StringUtil.isEmpty(goodsQueryParam.getKeyword())) {
				sqlBuffer.append(" and (g.goods_name like '%" + goodsQueryParam.getKeyword() + "%' or g.sn like '%"
						+ goodsQueryParam.getKeyword() + "%') ");
			}
		} else {
			// 高级搜索
			if (goodsQueryParam.getCategory_id() != null) {
				Category category = this.daoSupport.queryForObject(
						"select * from es_goods_category where category_id=? ", Category.class,
						goodsQueryParam.getCategory_id());
				if (category != null) {
					String cat_path = category.getCategory_path();
					if (cat_path != null) {
						sqlBuffer.append(" and  g.category_id in(");
						sqlBuffer.append("select c.category_id from es_goods_category");
						sqlBuffer.append(" c where c.category_path like '" + cat_path + "%')");
					}
				}
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_name())) {
				sqlBuffer.append(" and g.goods_name like '%" + goodsQueryParam.getGoods_name() + "%'");
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getSeller_name())) {
				sqlBuffer.append(" and g.seller_name like '%" + goodsQueryParam.getSeller_name() + "%'");
			}
			if (!StringUtil.isEmpty(goodsQueryParam.getGoods_sn())) {
				sqlBuffer.append(" and g.sn like '%" + goodsQueryParam.getGoods_sn() + "%'");
			}
		}
		sqlBuffer.append(" order by g.goods_id desc");
		Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPage_no(),
				goodsQueryParam.getPage_size());
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void authGoods(Integer goods_id, Integer pass, String message) {
		String sql = "update es_goods set is_auth=?,auth_message=?  where goods_id=? ";
		// 审核通过
		if (pass.intValue() == 1) {
			daoSupport.execute(sql, 2, message, goods_id);
		} else {
			daoSupport.execute(sql, 3, message, goods_id);
			// 商品失败消息
			GoodsReasonMsg goodsReasonMsg = new GoodsReasonMsg(new Integer[] { goods_id },
					GoodsReasonMsg.GOODS_VERIFY, message);
			this.amqpTemplate.convertAndSend(AmqpExchange.GOODS_CHANGE_REASON.name(), "goods-reason-routingKey",
					goodsReasonMsg);
		}
	}
}
