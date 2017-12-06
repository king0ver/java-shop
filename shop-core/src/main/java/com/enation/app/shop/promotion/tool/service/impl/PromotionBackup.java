package com.enation.app.shop.promotion.tool.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.tool.model.po.PromotionGoods;
import com.enation.app.shop.promotion.tool.service.IPromotionBackupManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

@Service
public class PromotionBackup implements IPromotionBackupManager {

	@Autowired
	private IDaoSupport daoSupport;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	@Transactional
	public void backup() {
		// 获取三个月前的日历÷
		Long end_time = DateUtil.getDateline();

		List<PromotionGoods> pgs = daoSupport.queryForList("select * from es_promotion_goods where end_time < ?",PromotionGoods.class,
				end_time);

		this.daoSupport.execute("delete from es_promotion_goods where end_time < ?", end_time);

		String sql = "insert into es_promotion_goods_backend values(?,?,?,?,?,?,?,?)";

		int[] ids = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return pgs.size();
			}
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1,pgs.get(i).getPg_id());
				ps.setInt(2,pgs.get(i).getGoods_id());
				ps.setInt(3,pgs.get(i).getProduct_id());
				ps.setLong(4,pgs.get(i).getStart_time());
				ps.setLong(5,pgs.get(i).getEnd_time());
				ps.setInt(6,pgs.get(i).getActivity_id());
				ps.setString(7,pgs.get(i).getPromotion_type());
				ps.setString(8,pgs.get(i).getTitle());
			} 
		});
	}

}
