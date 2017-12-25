package com.enation.app.nanshan.service.impl;

import com.enation.app.nanshan.service.RechargeService;
import com.enation.app.nanshan.vo.RechargeVo;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.service.ITradeSnCreator;
import com.enation.framework.database.IDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong on 17/12/23.
 */
@Service("rechargeService")
public class RechargeServiceImpl implements RechargeService {


    @Autowired
    private IDaoSupport iDaoSupport;

    @Autowired
    private ITradeSnCreator tradeSnCreator;


    @Override
    public RechargeVo create(String gameAccount, int points, String clientType, int memberId, String memberName) {


        RechargeVo rechargeVo = new RechargeVo();

        rechargeVo.setGame_account(gameAccount);
        rechargeVo.setClient_type(clientType);
        rechargeVo.setMember_id(memberId);
        rechargeVo.setPoints(points);
        rechargeVo.setMember_name(memberName);
        rechargeVo.setOrder_status(OrderStatus.NEW.description());
        rechargeVo.setCreate_time(System.currentTimeMillis());

       rechargeVo.setRecharge_sn(tradeSnCreator.generateRechargeSn());

        iDaoSupport.insert("es_recharge", rechargeVo);

        return rechargeVo;
    }
}
