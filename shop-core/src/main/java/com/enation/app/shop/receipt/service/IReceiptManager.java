package com.enation.app.shop.receipt.service;

import com.enation.app.shop.receipt.model.Receipt;

public interface IReceiptManager {

	public void add(Receipt invoice);
	
	public Receipt getById(Integer id);
	
	public Receipt getByOrderid(Integer orderid);
}
