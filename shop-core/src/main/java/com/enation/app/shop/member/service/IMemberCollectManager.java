package com.enation.app.shop.member.service;

import com.enation.app.shop.member.model.po.MemberCollect;
import com.enation.framework.database.Page;

/**
 * 会员收藏店铺接口
 * @author xulipeng
 * @since v6.4
 * @version v1.0
 * 2017年10月13日18:03:20
 */
public interface IMemberCollectManager {
	
	/**
	 * 添加店铺
	 * @param collect
	 */
	public void addCollect(MemberCollect collect);
	
	/**
	 * 删除收藏的店铺
	 * @param collect_id
	 */
	public void delCollect(Integer member_id,Integer store_id);
	
	/**
	 * 获取收藏店铺的集合
	 * @param memberid
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page getList(Integer memberid,int page,int pageSize);

	/**
	 * 是否已经收藏
	 * @param memberId 会员id
	 * @param storeId 店铺id
	 * @return
	 */
	public boolean isCollect(int memberId, int storeId);

	/**
	 * 查询店铺收藏
	 * @param celloct_id
	 * @return
	 */
	public MemberCollect getCollect(Integer celloct_id);
	
	/**
	 * 读取我收藏的店铺数量
	 * @param member_id
	 * @return
	 */
	public int getCount(Integer member_id);
}
