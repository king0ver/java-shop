package com.enation.app.nanshan.service;

import com.enation.app.nanshan.model.ArticleExt;
import com.enation.app.nanshan.model.NanShanActReserve;
import com.enation.app.nanshan.vo.ActReserveVo;
import com.enation.app.nanshan.vo.ArticleVo;
import com.enation.framework.database.Page;

public interface IActReserveService {
	

	/** 
	* @Description:预约接口，前端调用 
	* @author luyanfen  
	* @date 2017年12月18日 下午7:53:24
	*  
	*/ 
	public void reserve(NanShanActReserve NanShanActReserve);

	/** 
	* @param NanShanActReserve
	* @Description: 取消预约
	* @author luyanfen  
	* @date 2018年1月3日 下午2:06:30
	*  
	*/ 
	public void cancelReserve(NanShanActReserve NanShanActReserve);
	
	/** 
	* @param  activityId
	* @Description:  查询互动预约信息
	* @author luyanfen  
	* @date 2018年1月3日 上午11:36:50
	*  
	*/ 
	public ArticleExt queryArticleExt(int activityId);
	
	/**
	 * 用过用户ID查询预约信息
	 * @param userId
	 * @return
	 */
	public Page<ActReserveVo> queryReserveListById(Integer userId, String isDel, int pageNo, int pageSize);

	/**
	 * 查用户预约活动记录
	 * @param memberId
	 * @return
     */
	public NanShanActReserve queryReserveByMemberId(int memberId, int activityId);

}
