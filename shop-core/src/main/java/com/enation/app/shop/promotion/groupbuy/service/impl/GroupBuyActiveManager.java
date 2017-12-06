package com.enation.app.shop.promotion.groupbuy.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.goods.service.ITagManager;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuy;
import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyActive;
import com.enation.app.shop.promotion.groupbuy.model.vo.GroupBuyActiveVo;
import com.enation.app.shop.promotion.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.promotion.tool.model.enums.PromotionTypeEnum;
import com.enation.app.shop.promotion.tool.service.IPromotionGoodsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 团购活动管理类
 * @author Kanon
 *
 */
@Service("groupBuyActiveManager")
public class GroupBuyActiveManager implements IGroupBuyActiveManager {
	
	@Autowired
	private IDaoSupport daoSupport;
	
	@Autowired
	private ITagManager tagManager;
	
	@Autowired
	private IPromotionGoodsManager promotionGoodsManager;
	 
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#groupBuyActive(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Page groupBuyActive(Integer pageNo, Integer pageSize, Map map) {
		String sql ="select * from es_groupbuy_active order by add_time desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize,  GroupBuyActiveVo.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#add(com.enation.app.shop.component.groupbuy.model.GroupBuyActive)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(GroupBuyActive groupBuyActive) { 
		groupBuyActive.setAdd_time(DateUtil.getDateline()); 
		this.daoSupport.insert("es_groupbuy_active", groupBuyActive);
		//判断是否开启团购活动
		groupBuyActive.setAct_id(this.daoSupport.getLastId("es_groupbuy_active")); 
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#update(com.enation.app.shop.component.groupbuy.model.GroupBuyActive)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(GroupBuyActive groupBuyActive) {
		this.daoSupport.update("es_groupbuy_active", groupBuyActive, "act_id="+groupBuyActive.getAct_id());
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#delete(java.lang.Integer[])
	 */
	@Override
	public void delete(Integer[] ids) {
		String idstr = StringUtil.arrayToString(ids, ",");
		this.daoSupport.execute("delete from es_groupbuy_active where act_id in ("+idstr+")");
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#delete(int)
	 */
	@Override
	public void delete(int id) {  
		//删除团购同时删除活动商品表的商品信息
		String sql = "select * from es_groupbuy_goods where act_id = ? ";
		List<Map> queryForList = this.daoSupport.queryForList(sql, id);
		for (Map map : queryForList) {
			promotionGoodsManager.delete(Integer.parseInt(map.get("gb_id").toString()), PromotionTypeEnum.GROUPBUY.getType());
		}
		this.daoSupport.execute("delete from es_groupbuy_active where act_id=?", id);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#get(int)
	 */
	@Override
	public GroupBuyActiveVo get(int id) {
		return (GroupBuyActiveVo)this.daoSupport.queryForObject("select * from es_groupbuy_active where act_id=?", GroupBuyActiveVo.class, id);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#get()
	 */
	@Override
	public GroupBuyActiveVo get() {
//		TODO 这里的搜索语句有问题
		return (GroupBuyActiveVo)this.daoSupport.queryForObject("select * from es_groupbuy_active where end_time>? and start_time<? ", GroupBuyActiveVo.class, DateUtil.getDateline(), DateUtil.getDateline());
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#getLastEndTime()
	 */
	@Override
	public Long getLastEndTime() {
		return this.daoSupport.queryForLong("SELECT max(end_time) from es_groupbuy_active");
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#listEnable()
	 */
	@Override
	public List<GroupBuyActiveVo> listEnable() {
		String sql ="select * from es_groupbuy_active where end_time>=? order by add_time desc";
		long now = DateUtil.getDateline();
		return this.daoSupport.queryForList(sql, GroupBuyActiveVo.class,now);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager#listJoinEnable()
	 */
	@Override
	public List<GroupBuyActiveVo> listJoinEnable() {
		String sql ="select * from es_groupbuy_active where join_end_time>=? order by add_time desc";
		long now = DateUtil.getDateline();
		return this.daoSupport.queryForList(sql, GroupBuyActiveVo.class,now);
	}

	@Override
	public Integer checkAct(long start_time, long end_time) {
		String sql = "select count(*) from es_groupbuy_active  where  (start_time <= ? and end_time >= ?) or (start_time <= ? and end_time >= ?) or (start_time>= ? and end_time <= ?)";
		Integer queryForInt = this.daoSupport.queryForInt(sql, start_time,start_time,end_time,end_time,start_time,end_time);
		return queryForInt;
	}
	
}
