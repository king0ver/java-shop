package com.enation.app.shop.trade.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.po.TransactionRecord;
import com.enation.app.shop.trade.service.ITransactionRecordManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

/**
 * 交易记录
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月28日 下午2:41:38
 */
@Service
public class TransactionRecordManager implements ITransactionRecordManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	@Override
	public TransactionRecord add(TransactionRecord record) {
		
		this.daoSupport.insert("es_transaction_record", record);
		
		return record;
	}

	@Override
	public Page get(Integer pageNo,Integer pageSize,Integer goodsId) {
		// 获取交易记录
				String sql = "select * from  es_transaction_record where goods_id=? order by rog_time desc";
				List list = daoSupport.queryForListPage(sql, pageNo, pageSize, goodsId);

				// 获取总交易量
				sql="select count(*) from  es_transaction_record where goods_id=?";

				Integer count = daoSupport.queryForInt(sql, goodsId);
				Page page = new Page(pageNo, count, pageSize, list);
				return page;
	}

}
