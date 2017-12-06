package com.enation.app.shop.promotion.groupbuy.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyCat;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyCatManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 团购分类管理类
 * @author Chopper
 * @version v1.0
 * @since v6.4
 * 2017年8月29日 下午8:54:37 
 *
 */
@Service
public class GroupBuyCatManager implements IGroupBuyCatManager {

	@Autowired
	private IDaoSupport daoSupport;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager#list(int, int)
	 */
	@Override
	public Page list(int pageNo, int pageSize) {
		String sql ="select * from es_groupbuy_cat order by cat_order ";
		return this.daoSupport.queryForPage(sql,pageNo, pageSize,GroupBuyCat.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager#listAll()
	 */
	@Override
	public List<GroupBuyCat> listAll() {
		String sql ="select * from es_groupbuy_cat order by cat_order ";
		return this.daoSupport.queryForList(sql,GroupBuyCat.class);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager#get(int)
	 */
	@Override
	public GroupBuyCat get(int catid) {
		 
		return (GroupBuyCat)this.daoSupport.queryForObject("select * from es_groupbuy_cat where catid=?",  GroupBuyCat.class,catid);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager#add(com.enation.app.shop.component.groupbuy.model.GroupBuyCat)
	 */
	@Override
	public void add(GroupBuyCat groupBuyCat) {
		this.daoSupport.insert("es_groupbuy_cat", groupBuyCat);

	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager#update(com.enation.app.shop.component.groupbuy.model.GroupBuyCat)
	 */
	@Override
	public void update(GroupBuyCat groupBuyCat) {
		this.daoSupport.update("es_groupbuy_cat", groupBuyCat,"catid="+groupBuyCat.getCatid());
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager#delete(java.lang.Integer[])
	 */
	@Override
	public void delete(Integer[] catid) {
		if(catid==null || catid.length==0) return;
		String id_str = StringUtil.arrayToString(catid, ",");
		this.daoSupport.execute("delete from es_groupbuy_cat where catid in ("+id_str+")");
	}

}
