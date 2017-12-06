package com.enation.app.shop.promotion.groupbuy.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.promotion.groupbuy.model.po.GroupBuyActive;
import com.enation.app.shop.promotion.groupbuy.model.vo.GroupBuyActiveVo;
import com.enation.framework.database.Page;

/**
 * 
 * @ClassName: IGroupBuyActiveManager 
 * @Description: 团购活动管理类 
 * @author TALON 
 * @date 2015-7-31 上午1:38:39 
 *
 */
public interface IGroupBuyActiveManager {
	/**
	 * 团购活动列表
	 * @param page 分页页码
	 * @param pageSize 分页每页显示数量
	 * @param map 搜索条件
	 * @return 团购活动分页列表
	 */
	public Page groupBuyActive(Integer page,Integer pageSize,Map map);
	
	/**
	 * 添加团购活动
	 * @param groupBuyActive 团购活动
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(GroupBuyActive groupBuyActive);
	
	/**
	 * 更新团购活动
	 * @param groupBuyActive 团购活动
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(GroupBuyActive groupBuyActive);
	
	/**
	 * 批量删除团购活动 
	 * @param ids 团购活动Id
	 */
	public void delete(Integer[] ids);
	
	/**
	 * 删除某个团购活动
	 * @param id 团购活动Id
	 */
	public void delete(int id);
	/**
	 * 获取某个团购活动
	 * @param id 团购活动Id
	 * @return GroupBuyActive 团购活动
	 */
	public GroupBuyActiveVo get(int id);
	/**
	 * 获取当前正在举办的团购活动
	 * @return GroupBuyActive 团购活动
	 */
	public GroupBuyActiveVo get();
	/**
	 * 获取团购最后结束日期
	 * @return Long 团购活动最后结束时间
	 */
	public Long getLastEndTime();
	
	/**
	 * 读取可用的团购活动（未过期的）
	 * @return List<GroupBuyActive> 团购活动列表（未过期）
	 */
	public List<GroupBuyActiveVo> listEnable();
	/**
	 * 读取可报名的团购活动
	 * @return List<GroupBuyActive> 可报名团购活动列表
	 */
	public List<GroupBuyActiveVo> listJoinEnable();
	/**
	 * 检测活动是否冲突
	 * @return
	 */
	public Integer checkAct(long start_time,long end_time);
}
