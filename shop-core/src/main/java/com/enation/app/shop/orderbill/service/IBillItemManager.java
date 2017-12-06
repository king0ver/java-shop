package com.enation.app.shop.orderbill.service;

import java.util.List;

import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.framework.database.Page;

/**
 * 
 * 结算单项管理接口
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午2:51:01
 */
public interface IBillItemManager {

	/**
	 * 新增一条单项
	 * @param billItem 实体对象
	 */
	public void add(BillItem billItem);
	
	/**
	 * 修改状态
	 * 这里按照结算账单的id就行，单项的所有状态跟着结算账单
	 * 所以只需要传递bill_id
	 * @param billId 账单id
	 * @param status 状态
	 */
	public void editStatus(Integer billId, Integer status);
	
	
	/**
	 * 根据卖家ID和状态获取结算单项列表
	 * @param sellerId 		卖家id
	 * @param statusIndex   状态
	 * @param endTime		结束时间
	 * @return BillItem 集合
	 */
	public List<BillItem> getList(Integer sellerId, Integer statusIndex, String endTime);
	
	/**
	 * 与结算单-绑定关系
	 * @param sellerId 	卖家id
	 * @param billId	 结算单id
	 * @param endTime		结束时间
	 */
	public void bindingBill(Integer sellerId, Integer billId, String endTime);

	/**
	 * 查询结算单关联的订单
	 * @param bill_id 结算单id
	 * @param item_type 单项类型 0:收款，1=退款
	 * @param page_size 每页数量 
	 * @param page_no 页码
	 * @return BillItem 集合
	 */
	public Page<BillItem> getItemList(Integer bill_id, Integer item_type, Integer page_no, Integer page_size);
	
}
