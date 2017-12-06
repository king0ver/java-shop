package com.enation.app.shop.comments.plugin;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.comments.model.po.ShopScore;
import com.enation.app.shop.comments.service.IMemberCommentManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegister;
import com.enation.framework.util.CurrencyUtil;

/**
 * 计算店铺的评分
 * 
 * @author dongxin
 * @version v1.0
 * @since v6.4.0 2017年11月29日 下午1:22:08
 */
@Component
@AutoRegister
public class CalculateShopScorePlugin implements IEveryDayExecuteEvent {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private IMemberCommentManager memberCommentManager;

	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public void everyDay() {
		try {
			// 计算动态平均的评分添入数据库
			this.getShopGrade();
		} catch (Exception e) {
			logger.error("计算店铺动态评分出错", e);
		}
	}

	private void getShopGrade() {
		String sql = "select shop_id , AVG(shop_desccredit) as shop_desccredit ,AVG(shop_servicecredit) as shop_servicecredit , AVG(shop_deliverycredit) as shop_deliverycredit"
				+ " from es_shop_score group by shop_id";
		List<ShopScore> shopScoreList = this.daoSupport.queryForList(sql,ShopScore.class);
		for (ShopScore shopScore : shopScoreList) {
			String updatesql = "update es_shop_detail set shop_desccredit=?, shop_servicecredit=?,,shop_deliverycredit =? where shop_id=? ";
			this.daoSupport.execute(updatesql, CurrencyUtil.round(shopScore.getShop_desccredit(), 1),
					CurrencyUtil.round(shopScore.getShop_servicecredit(), 1),
					CurrencyUtil.round(shopScore.getShop_deliverycredit(), 1), shopScore.getShop_id());
		}
	}

}
