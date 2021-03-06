package com.enation.app.shop.receipt.service;

import java.util.List;

import com.enation.app.shop.receipt.model.ReceiptContent;
import com.enation.app.shop.trade.model.po.OrderPo;
import com.enation.framework.database.Page;

/**
 * 
 * 发票内容管理
 * @author wanglu
 * @version v6.5.0
 * @since v6.5.1
 * 2017年7月1日 下午5:48:29
 */
public interface IReceiptContentManager {
	/**
	 * 添加发票内容
	 * @param receiptContent 发票内容，ReceiptContent
	 */
	public ReceiptContent saveAdd(ReceiptContent receiptContent);
	/**
	 * 修改发票内容
	 * @param receiptContent 发票内容，ReceiptContent
	 */
	public void saveEdit(ReceiptContent receiptContent);
	/**
	 * 获取发票内容
	 * @param contentid 发票内容ID,Integer
	 * @return ReceiptContent
	 */
	public ReceiptContent get(Integer contentid);
	
	/**
	 * 获取发票内容分页列表
	 * @param pageNo 页数,Integer
	 * @param pageSize 每页显示数量,Integer
	 * @return 文章分页列表
	 */
	public Page getAllReceiptContent(Integer pageNo, Integer pageSize);
	
	/**
	 * 删除发票内容
	 * @param contentid, 发票内容ID,Integer
	 */
	public void delete(Integer contentid);
	/**
	 * 检查有几个发票内容
	 *
	 */
	public int checkLast();
	
	/**
	 * 检查发票内容名字是否重复
	 * @param receiptContent 发票内容,ReceiptContent
	 * @return boolean
	 */
	public boolean is_exist(ReceiptContent receiptContent);
	
	/**
	 * 获取发票内容集合
	 * @return 发票内容List
	 */
	public List<ReceiptContent> listReceiptContent();
	
	
	/**
	 * 获取历史发票的分页列表
	 * @param pageNo 页数,Integer
	 * @param pageSize 每页显示数量,Integer
	 * @return 历史发票分页列表
	 */
	public Page getHistoryReceipt(Integer pageNo, Integer pageSize);
	
	/**
	 * 获取某店铺的历史发票
	 * @param pageNo 页数
	 * @param pageSize 每页显示数量
	 * @param member_id 会员id
	 * @return 历史发票分页列表
	 */
	public Page getCurrentHistoryReceipt(Integer pageNo, Integer pageSize,Integer seller_id);
	
	/**
	 * 获取历史发票详情
	 * @param order_id  订单id
	 * @return 历史订单发票详情
	 */
	public OrderPo getHistory(String order_id);
}
