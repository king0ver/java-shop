package com.enation.app.shop.receipt.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.receipt.model.ReceiptContent;
import com.enation.app.shop.receipt.service.IReceiptContentManager;
import com.enation.app.shop.trade.model.enums.ReceiptType;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

@Service("receiptContentManager")
public class ReceiptContentManager implements IReceiptContentManager{
	
	@Autowired
	private IDaoSupport daoSupport;

	@Override
	public ReceiptContent saveAdd(ReceiptContent receiptContent) {
		
		this.daoSupport.insert("es_receipt_content", receiptContent);
		
		return receiptContent;
	}

	@Override
	public void saveEdit(ReceiptContent receiptContent) {

		this.daoSupport.update("es_receipt_content", receiptContent, "contentid =" + receiptContent.getContentid());
		
	}

	@Override
	public ReceiptContent get(Integer contentid) {
		
		return this.daoSupport.queryForObject("select * from es_receipt_content where contentid=?", ReceiptContent.class, contentid);
	}

	@Override
	public Page getAllReceiptContent(Integer pageNo, Integer pageSize){

		String sql = "select receipt_content,contentid from es_receipt_content";

		return this.daoSupport.queryForPage(sql, pageNo, pageSize, null);
	}

	@Override
	public void delete(Integer contentid) {
		
		if (this.checkLast() == 1) {
			throw new RuntimeException("必须最少保留一个发票内容");
		}
		
		this.daoSupport.execute("delete from es_receipt_content where contentid=?", contentid);
		
	}
	
	@Override
	public int checkLast() {
		int count = this.daoSupport.queryForInt("select count(0) from es_receipt_content");
		return count;
	}
	
	public boolean is_exist(ReceiptContent receiptContent) {
		
		boolean flag = false;
		
		String sql="select count(0) from es_receipt_content where receipt_content = ?";
		
		int i =  this.daoSupport.queryForInt(sql, receiptContent.getReceipt_content());
		
		if(i!=0){
			flag=true;
		}
		return flag;
	}
	
	@Override
	public List<ReceiptContent> listReceiptContent() {
		List<ReceiptContent> list = this.daoSupport.queryForList(
				"SELECT * FROM es_receipt_content", ReceiptContent.class);
		return list;
	}

	@Override
	public Page getHistoryReceipt(Integer pageNo, Integer pageSize) {
		String sql ="select o.*, m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.need_receipt = 'yes' order by o.create_time desc";
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, null);
		List<Map> list = (List) page.getResult();
		if(list!=null&&list.size()>0){
			for(Map map : list){
				map.put("receipt_type", ReceiptType.valueOf(map.get("receipt_type").toString()).description());
			}
		}
		return page;
		
		
	}

	@Override
	public OrderPo getHistory(String order_id) {
		String sql = "select o.*, m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.order_id = ?";
		
		return this.daoSupport.queryForObject(sql, OrderPo.class, order_id);
	}

	@Override
	public Page getCurrentHistoryReceipt(Integer pageNo, Integer pageSize, Integer seller_id) {
		String sql ="select o.*, m.uname from es_order o left join es_member m on o.member_id = m.member_id where o.seller_id = ? and o.need_receipt = 'yes'  order by o.create_time desc";
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, seller_id);
		List<Map> list = (List) page.getResult();
		if(list!=null&&list.size()>0){
			for(Map map : list){
				map.put("receipt_type", ReceiptType.valueOf(map.get("receipt_type").toString()).description());
			}
		}
		return page;
		
		
	}

}
