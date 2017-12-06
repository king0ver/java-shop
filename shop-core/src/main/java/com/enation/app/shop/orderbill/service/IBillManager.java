package com.enation.app.shop.orderbill.service;

import com.enation.app.shop.orderbill.model.po.Bill;
import com.enation.framework.database.Page;

/**
 * 
 * 结算单接口
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月16日 下午2:52:48
 */
public interface IBillManager {

	
	/**
	 * 账单号是否存在
	 * @param sn 账单号
	 * @return true存在 false不存在
	 */
	public boolean isHaveSn(String sn);
	
	/**
	 * 保存一个新账单
	 * @param bill 实体
	 */
	public void saveBill(Bill bill);
	
	/**
	 * 通过账单id和卖家id 获取账单
	 * @param bill_id  账单id
	 * @param seller_id 卖家id
	 */
	public Bill getBill(Integer bill_id, Integer seller_id);
	
	/**
	 * 改变账单状态
	 * @param bill_id  账单id
	 * @param status 状态值
	 */
	public void editStatus(Integer bill_id, Integer status);

	/**
	 * 商家查询我的结算单列表
	 * @param page_no 页码
	 * @param page_size 每页数量
	 * @return Page<Bill>
	 */
	public Page<Bill> queryMyShopBill(Integer page_no, Integer page_size);

	/**
	 * 修改账单为已支付状态
	 * @param bill_id 账单id
	 */
	public void editBillPayStatus(Integer bill_id);

	/**
	 * 查询结算单的详细
	 * @param bill_id 账单id
	 * @return Bill实体
	 */
	public Bill get(Integer bill_id);

	/**
	 * 周期 查看结算单列表
	 * @param page_no 页码
	 * @param page_size 页码大小
	 * @return Page<Bill>
	 */
	public Page<Bill> getAllBill(Integer page_no, Integer page_size);

	/**
	 * 
	 * @param page_no 页码
	 * @param page_size 页码大小
	 * @param sn 账单号
	 * @return Page<Bill>
	 */
	public Page<Bill> getBillDetail(Integer page_no, Integer page_size, String sn);

	/**
	 * 定时穿件结算单
	 * @return Bill 实体
	 */
	public Bill createBills();
	
}
