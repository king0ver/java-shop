package com.enation.app.shop.trade.service;

import java.util.List;

import com.enation.app.shop.trade.model.po.TransactionRecord;
import com.enation.framework.database.Page;

/**
 * 交易记录
 * @author fk
 * @version v6.4
 * @since v6.4
 * 2017年9月28日 下午2:38:40
 */
public interface ITransactionRecordManager {

	/**
	 * 添加交易记录
	 * @param record
	 * @return
	 */
	public TransactionRecord add(TransactionRecord record);
	
	/**
	 * 查询交易记录
	 * @param pageNo
	 * @param pageSize
	 * @param goodsId
	 * @return
	 */
	public Page get(Integer pageNo,Integer pageSize,Integer goodsId);
}
