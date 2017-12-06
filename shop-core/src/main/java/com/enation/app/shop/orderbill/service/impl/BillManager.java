package com.enation.app.shop.orderbill.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.orderbill.model.enums.BillStatusEnum;
import com.enation.app.shop.orderbill.model.po.Bill;
import com.enation.app.shop.orderbill.model.po.BillItem;
import com.enation.app.shop.orderbill.service.IBillItemManager;
import com.enation.app.shop.orderbill.service.IBillManager;
import com.enation.app.shop.shop.apply.model.vo.ShopBankVo;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.framework.validator.ErrorCode;
import com.enation.framework.validator.NoPermissionException;

/**
 * 结算单项实现manager
 * 
 * @author yanlin
 * @version v1.0
 * @since v6.4.0
 * @date 2017年8月15日 下午1:17:34
 */
@Service
public class BillManager implements IBillManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private IBillItemManager billItemManager;

	@Autowired
	private ISellerManager SellerMangager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.orderbill.service.IBillManager#isHaveSn(java.lang.String)
	 */
	@Override
	public boolean isHaveSn(String sn) {
		String sql = "SELECT COUNT(id) FROM es_bill WHERE bill_sn = ? ";
		Integer count = this.daoSupport.queryForInt(sql, sn);
		return count > 0 ? true : false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.javashop.manager.IBillManager#saveBill(com.enation.javashop.entity.Bill)
	 */
	@Override
	public void saveBill(Bill bill) {
		
		Assert.notNull(bill, "账单不能为空");
		
		this.daoSupport.insert("es_bill", bill);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.javashop.manager.IBillManager#getBill(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Bill getBill(Integer bill_id, Integer seller_id) {
		
		Assert.notNull(bill_id, "账单Id不能为空");
		Assert.notNull(seller_id, "卖家Id不能为空");
		
		String sql = "SELECT * FROM es_bill WHERE seller_id = ? AND id = ?";
		return this.daoSupport.queryForObject(sql, Bill.class, seller_id, bill_id);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.javashop.manager.IBillManager#editStatus(java.lang.Integer, java.lang.Integer)
	 */
	
	@Override
	public void editStatus(Integer bill_id, Integer status) {
		
		Assert.notNull(bill_id, "账单ID不能为空");
		Assert.notNull(status, "状态不能为空");
		String sql  = "UPDATE es_bill SET status = ? WHERE id = ?";
		this.daoSupport.execute(sql, status, bill_id);
		
		// 修改结算单项状态
		this.billItemManager.editStatus(bill_id, status);
		
	}

	@Override
	public Page<Bill> queryMyShopBill(Integer page_no, Integer page_size) {
		//判断卖家是否登录
		Seller member = SellerMangager.getSeller();
		if (member == null) {
			throw new NoPermissionException(ErrorCode.NO_PERMISSION, "未登录，无权限");
		}
		
		String sql = "select * from es_bill where seller_id = ?";
		return this.daoSupport.queryForPage(sql, page_no, page_size, member.getStore_id());
	}

	@Override
	public void editBillPayStatus(Integer bill_id) {
		Assert.notNull(bill_id, "账单ID不能为空");
		
		String sql  = "UPDATE es_bill SET status = ? ,pay_time=? WHERE id = ?";
		this.daoSupport.execute(sql, BillStatusEnum.PAY.getIndex(),DateUtil.getDateline(), bill_id);
		// 修改结算单项状态
		this.billItemManager.editStatus(bill_id, BillStatusEnum.PAY.getIndex());
	}

	@Override
	public Bill get(Integer bill_id) {
		String sql = "select * from es_bill where id = ?";
		return this.daoSupport.queryForObject(sql, Bill.class, bill_id);
	}

	@Override
	public Page getAllBill(Integer page_no, Integer page_size) {
		String sql = "select sn,start_time,end_time,sum(price) price,sum(commi_price)commi_price,sum(discount_price)discount_price,sum(bill_price)bill_price,sum(returned_price)returned_price,SUM(returned_commi_price)returned_commi_price "
				+ "from es_bill group by sn ,start_time,end_time ";
		return this.daoSupport.queryForPage(sql, page_no, page_size);
	}

	@Override
	public Page getBillDetail(Integer page_no, Integer page_size, String sn) {
		String sql = "select * from  es_bill where sn = ?";
		return this.daoSupport.queryForPage(sql, page_no, page_size,sn);
	}

	@Override
	public Bill createBills() {
		String sql = "select seller_id from es_bill_item  group by seller_id ";
		List<Map> list = this.daoSupport.queryForList(sql);
		for(Map map : list){
			Object seller_id = map.get("seller_id");
			
			this.createBillBySeller(Integer.valueOf(seller_id.toString()));
			
		}
		return null;
	}

	private Bill createBillBySeller(Integer seller_id) {
		//获取店铺银行信息
		
		String sql = "select s.shop_id seller_id,s.shop_name,d.shop_commission,d.bank_account_name,d.bank_number,"
				+ "d.bank_name,d.bank_province,d.bank_city,d.bank_region,d.bank_town"
				+ " from es_shop s inner join es_shop_detail d  on s.shop_id = d.shop_id where s.shop_id = ?";
		ShopBankVo shop = this.daoSupport.queryForObject(sql, ShopBankVo.class, seller_id);
		
		Long[] time = DateUtil.getLastMonth();//上个月的开始结束时间
		
		String lastTime = String.valueOf(time[1]);
		// 结算单类型
		Integer billType = 0;//TODO
		
		// 1.构建结算单对象
		Bill bill = new Bill(time[0], billType, seller_id,time[1]);
		String billSn = "";
		boolean isHave = false;
		
		{
			billSn = createBillSn();
			isHave = this.isHaveSn(billSn);
		}while(isHave);
		
		bill.setBill_sn(billSn);
		
		bill.setSn(billSn.substring(0,billSn.length()-6));
		
		// 2.得到账单单项 开始遍历统计
		List<BillItem> list = this.billItemManager.getList(seller_id, BillStatusEnum.NEW.getIndex(), lastTime);
		
		Double price = 0.00d;
		Double commiPrice = 0.00d;
		Double discountPrice = 0.00d;
		Double returnedPrice = 0.00d;
		Double billPrice = 0.00d;
		Double returnedCommiPrice = 0.00d;
		
		// 佣金比例
		Double commiRate = shop.getShop_commission()/100;
		for(BillItem item : list) {
			
			Integer itemType = item.getItem_type();//0:收款，1=退款
			Double priceItem = item.getOrder_price();
			Double discountPriceItem = item.getDiscount_price();//优惠金额
			if (discountPriceItem == null) {
				discountPriceItem = 0.00d;
			}
			
			price = CurrencyUtil.add(price, priceItem);//订单金额，包括收款和退款
			// 0为正常订单 1为退款订单
			if (itemType.intValue() == 1) {
				returnedPrice = CurrencyUtil.add(returnedPrice, priceItem);//退单总金额
			}
 			
			discountPrice = CurrencyUtil.add(discountPrice, discountPriceItem);
		}
		
		commiPrice = CurrencyUtil.mul(price, commiRate);
		returnedCommiPrice = CurrencyUtil.mul(returnedPrice, commiRate);
		
		billPrice = price-returnedPrice-commiPrice+returnedCommiPrice;
		
		bill.setPrice(price);
		bill.setDiscount_price(discountPrice);
		bill.setCommi_price(commiPrice);
		bill.setBill_price(billPrice);
		bill.setReturned_commi_price(returnedCommiPrice);
		bill.setReturned_price(returnedPrice);
		//保存店铺的银行相关信息
		bill.setBank_account_name(shop.getBank_account_name());
		bill.setBank_account_number(shop.getBank_number());
		
		String address = shop.getBank_province() + "- "+ shop.getBank_city() + "-" + shop.getBank_region();
		if(shop.getBank_town() != null){
			address += "-" + shop.getBank_town();
		} 
		
		bill.setBank_address(address);
		bill.setBank_code(shop.getBank_code());
		bill.setBank_name(shop.getBank_name());
		bill.setShop_name(shop.getShop_name());
		
		// 保存结算单
		this.saveBill(bill);
		Integer billId  = this.daoSupport.getLastId("es_bill");
		bill.setId(billId);
		
		// 之前查找的数据没有绑定, 就直接绑定了
		this.billItemManager.bindingBill(seller_id, billId, lastTime);
		
		// 生成完毕后 结算单出账
		this.editStatus(billId, BillStatusEnum.OUT.getIndex());
		
		return bill;
	}
	
	/**
	 * 新建一个订单编号
	 * @return
	 */
	private String createBillSn() {
		// 这里后期可以改为像创建订单一样 用redis
		String date = DateUtil.toString(DateUtil.getDateline(), "yyyyMMdd");
		String dateline = StringUtil.toString(DateUtil.getDateline());
		dateline = dateline.substring(dateline.length() - 5, dateline.length());
		Random random = new Random();
		String sn = "B" + date + dateline + random.nextInt(5);
		return sn;
	}

}
