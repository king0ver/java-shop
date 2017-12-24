package com.enation.app.nanshan.service;

import com.enation.app.nanshan.vo.RechargeVo;

/**
 * Created by yulong on 17/12/23.
 */
public interface RechargeService {

    /**
     * 创建充值订单
     * @return
     */
    RechargeVo create(String gameAccount, int points, String clientType, int memberId, String memberName);
}
