package com.enation.app.shop.comments.plugin;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.comments.service.IMemberCommentManager;
import com.enation.app.shop.goods.model.po.Goods;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.util.CurrencyUtil;

/**
 * 计算商品的好评率
 * 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0 2017年11月29日 下午1:21:46
 */
@Component
@AutoRegister
public class CalculateGoodsGradePlugin implements IEveryDayExecuteEvent {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private IMemberCommentManager memberCommentManager;

	@Override
	public void everyDay() {
		try {
			// 计算好评率并添入数据库
			this.getGoodsGrade();
		} catch (Exception e) {
			logger.error("获取商品评分出错", e);
		}
	}

	private void getGoodsGrade() {
		String sql = "select goods_id, sum( CASE grade WHEN 3 THEN 1 ELSE 0  END ) /count(*) grade\n"
				+ "from es_member_comment where type=1 and status=1 group by goods_id ";
		List<Goods> goodsList = this.daoSupport.queryForList(sql,Goods.class);
		for (Goods goods : goodsList) {
			String updatesql = "update es_goods set grade=? where goods_id=?";
			double grade = CurrencyUtil.mul(goods.getGrade(), 100);
			this.daoSupport.execute(updatesql, CurrencyUtil.round(grade, 1), goods.getGoods_id());
		}

	}
}
