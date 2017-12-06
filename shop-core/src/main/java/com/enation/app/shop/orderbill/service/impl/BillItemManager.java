package com.enation.app.shop.orderbill.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillItemManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

/**
 * 
 * 结算单实现
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0 
 * @date 2017年8月15日 下午2:46:06
 */
@Service
public class BillItemManager implements IBillItemManager {

	@Autowired
	private IDaoSupport daoSupport;

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.orderbill.service.IBillItemManager#add(com.enation.app.shop.orderbill.model.entity.BillItem)
	 */
	@Override
	public void add(BillItem billItem) {
		try {
			Assert.notNull(billItem, "传入结算单项为null");
			this.daoSupport.insert("es_bill_item", billItem);

		} catch (RuntimeException e) {
			throw new RuntimeException("新增结算单项是出错:" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.orderbill.service.IBillItemManager#editStatus(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void editStatus(Integer billId, Integer status) {
		try {

			Assert.notNull(billId, "结算单id为null");
			Assert.notNull(status, "状态为null");

			// 这里按照结算账单的id就行，单项的所有状态跟着结算账单
			String sql = "UPDATE es_bill_item SET status = ? WHERE bill_id = ?";
			this.daoSupport.execute(sql, status, billId);
		} catch (Exception e) {
			throw new RuntimeException("修改结算单项时出错:" + e.getMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.orderbill.service.IBillItemManager#getList(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public List<BillItem> getList(Integer sellerId, Integer statusIndex, String endTime) {
		try {
			Assert.notNull(sellerId, "卖家id为null");
			Assert.notNull(statusIndex, "状态为null");

			String sql = "SELECT * FROM es_bill_item WHERE seller_id = ? AND status = ? AND add_time < ?";
			List<BillItem> list = this.daoSupport.queryForList(sql, BillItem.class, sellerId, statusIndex, endTime);
			return list;
		} catch (Exception e) {
			throw new RuntimeException("获取结算单项时出错:" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.orderbill.service.IBillItemManager#bindingBill(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public void bindingBill(Integer sellerId, Integer billId, String endTime) {
		Assert.notNull(sellerId, "卖家id为null");
		Assert.notNull(billId, "结算单id为null");

		String sql = "UPDATE es_bill_item SET bill_id = ? WHERE seller_id = ? AND bill_id IS NULL AND add_time < ?";
		this.daoSupport.execute(sql, billId, sellerId, endTime);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.orderbill.service.IBillItemManager#getItemList(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page<BillItem> getItemList(Integer bill_id, Integer item_type, Integer page_no, Integer page_size) {

		String sql = "select * from es_bill_item where item_type = ? and bill_id = ?";

		return this.daoSupport.queryForPage(sql, page_no, page_size, item_type, bill_id);
	}

}
