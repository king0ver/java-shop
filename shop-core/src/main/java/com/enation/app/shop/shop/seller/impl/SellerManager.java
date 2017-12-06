package com.enation.app.shop.shop.seller.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.base.core.model.Seller;
import com.enation.app.shop.shop.seller.ISellerManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;

@Service
public class SellerManager implements ISellerManager {
	@Autowired
	private IDaoSupport daoSupport;

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.member.service.ISellerManager#getSeller(java.lang.Integer)
	 */
	@Override
	public Seller getSeller(Integer member_id) {
		String sql = "select * from es_member where member_id=? and disabled!=1";
		return (Seller) this.daoSupport.queryForObject(sql, Seller.class, member_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.member.service.ISellerManager#getSeller()
	 */
	@Override
	public Seller getSeller() {
		HttpSession session = ThreadContextHolder.getSession();
		if (session != null) {
			Seller member = (Seller) session.getAttribute(ISellerManager.CURRENT_STORE_MEMBER_KEY);
			return member;
		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.member.service.ISellerManager#getSeller(java.lang.String)
	 */
	@Override
	public Seller getSeller(String member_name) {
		String sql = "select * from es_member where uname=? and disabled!=1";
		return (Seller) this.daoSupport.queryForObject(sql, Seller.class, member_name);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.member.service.ISellerManager#getMyStoreMembers(java.lang.Integer)
	 */
	@Override
	public List getMyStoreMembers(Integer store_id) {
		String sql = " select member_id,uname from es_member where store_id = " + store_id;
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public void updateSeller(Seller seller) {
		this.daoSupport.update("es_member", seller, "member_id = "+seller.getMember_id());
	}

	@Override
	public boolean verification(Integer seller_id) {
		Seller seller = this.getSeller();
		if(seller==null) {
			return false;
		}else if(seller.getStore_id().equals(seller_id)) {
			return true;
		} 
		return false;
	}

}
