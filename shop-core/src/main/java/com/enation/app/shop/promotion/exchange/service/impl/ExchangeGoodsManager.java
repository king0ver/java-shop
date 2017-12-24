package com.enation.app.shop.promotion.exchange.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.model.vo.GoodsQueryParam;
import com.enation.app.shop.promotion.exchange.service.IExchangeGoodsManager;
import com.enation.framework.annotation.Log;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.log.LogType;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.UnProccessableServiceException;

/**
 * 
 * 积分商品操作功能的实现
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年10月26日 下午4:24:47
 */
@Service
public class ExchangeGoodsManager implements IExchangeGoodsManager {
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public Page list(GoodsQueryParam goodsQueryParam) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select g.goods_id,g.goods_name,g.sn,g.thumbnail,g.seller_name,g.enable_quantity,g.quantity,g.price,g.create_time,g.sord,g.market_enable ,b.`name` brand_name,c.`name` category_name "
						+ "from es_goods g left join es_exchange_setting s on  s.goods_id=g.goods_id left join es_exchange_goods_cat c on c.category_id = s.category_id left join es_brand b on g.brand_id = b.brand_id "
						+ "  where  g.disabled = 0 and g.market_enable=1 and g.goods_type = 'point'");
		if (goodsQueryParam.getStype().intValue() == 0) {
			if (!StringUtil.isEmpty(goodsQueryParam.getKeyword())) {
				sqlBuffer.append(" and (g.goods_name like '%" + goodsQueryParam.getKeyword() + "%' or g.sn like '%"
						+ goodsQueryParam.getKeyword() + "%') ");
			}
		} else {
			// 高级搜索
			if (goodsQueryParam.getCategory_id() != null) {
				sqlBuffer.append(" and  c.category_id in( " + goodsQueryParam.getCategory_id() + " )");
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
		sqlBuffer.append(" order by g.sord asc");
		Page page = this.daoSupport.queryForPage(sqlBuffer.toString(), goodsQueryParam.getPage_no(),
				goodsQueryParam.getPage_size());
		return page;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	@Log(type = LogType.SETTING, detail = "修改积分商品排序")
	public void updateSort(Integer[] goods_id, Integer[] sord) {
		if (goods_id != null && sord != null && sord.length == goods_id.length) {
			String sql = "update es_goods set sord = ? where goods_id = ?";
			for (int i = 0; i < goods_id.length; i++) {
				if (sord[i] == null) {
					throw new UnProccessableServiceException(ErrorCode.GOODS_PARAM_ERROR, "排序有空值，请正确输入");
				}
				this.daoSupport.execute(sql, sord[i], goods_id[i]);
			}
		}
	}

	@Override
	public Page frontList(Integer pageNo, Integer pageSize, Integer cat_id) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(
				"select g.goods_name,g.sn,g.thumbnail,g.seller_name,g.buy_count,g.enable_quantity,g.quantity,g.price,g.create_time,g.sord,g.market_enable , "
						+ " s.* from es_goods g   left join es_exchange_setting s on "
						+ " s.goods_id=g.goods_id where  g.disabled = 0 and g.market_enable=1 and s.enable_exchange=1 and g.goods_type = 'point'");
		List<Object> term = new ArrayList<Object>();
		if (cat_id != null) {
			term.add(cat_id);
			sqlBuffer.append(" and  s.category_id =?");

		}
		sqlBuffer.append(" order by g.sord asc");
		Page list = this.daoSupport.queryForPage(sqlBuffer.toString(), pageNo, pageSize, term.toArray());
		return list;
	}

}
