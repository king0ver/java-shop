package com.enation.app.nanshan.core.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enation.app.nanshan.core.model.RechargeRecordVo;
import com.enation.app.nanshan.core.service.IRechargeManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;

/**
 * 充值服务接口实现
 * @author jianjianming
 * @version $Id: RechargeManagerImpl.java,v 0.1 2018年1月3日 下午3:14:11$
 */
@Service("rechargeManager")
public class RechargeManagerImpl implements IRechargeManager {

	@Autowired
	private IDaoSupport daoSupport;

	/**
	 * 分页查询充值记录信息
	 * @param params
	 * @return
	 * @see com.enation.app.nanshan.core.service.IRechargeManager#queryRechargeRecordList(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Page<RechargeRecordVo> queryRechargeRecordList(Map<String, Object> params,int page,int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select");
		sql.append(" t.recharge_id rechargeId,t.recharge_sn rechargeSn,t.member_id memberId,t.member_name memberName,");
		sql.append("t.points,t.game_account gameAccount,t.price,t.order_status orderStatus,");
		sql.append("t.pay_status payStatus,t.payment_type paymentType,t.creation_time creationTime,");
		sql.append("t.pay_order_no payOrderNo,t.payment_time paymentTime,t.client_type clientType ");
		sql.append("from es_nanshan_recharge_record t ");
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}

}
