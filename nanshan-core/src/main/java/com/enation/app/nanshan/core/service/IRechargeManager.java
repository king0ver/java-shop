package com.enation.app.nanshan.core.service;

import java.util.Map;

import com.enation.app.nanshan.core.model.RechargeRecordVo;
import com.enation.framework.database.Page;

/**
 * 充值服务接口
 * @author jianjianming
 * @version $Id: IRechargeManager.java,v 0.1 2018年1月3日 下午3:11:38$
 */
public interface IRechargeManager {
	
	/**
	 * 分页查询充值记录信息
	 * @param params
	 * @return
	 */
	public Page<RechargeRecordVo> queryRechargeRecordList(Map<String, Object> params,int page,int pageSize);


}
