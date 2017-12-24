package com.enation.app.nanshan.service.impl;

import com.enation.app.nanshan.service.RechargeService;
import com.enation.app.nanshan.vo.RechargeVo;
import com.enation.app.shop.trade.model.enums.OrderStatus;
import com.enation.app.shop.trade.service.ITradeSnCreator;
import com.enation.framework.database.IDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yulong on 17/12/23.
 */

public class RechargeServiceImpl implements RechargeService {


    @Autowired
    private IDaoSupport iDaoSupport;

    @Autowired
    private ITradeSnCreator tradeSnCreator;


    @Override
    public RechargeVo create(String gameAccount, int points, String clientType, int memberId, String memberName) {


        RechargeVo rechangeVo = new RechargeVo();

        rechangeVo.setGame_account(gameAccount);
        rechangeVo.setClient_type(clientType);
        rechangeVo.setMember_id(memberId);
        rechangeVo.setPoints(points);
        rechangeVo.setMember_name(memberName);
        rechangeVo.setOrder_status(OrderStatus.NEW.description());
        rechangeVo.setCreate_time(System.currentTimeMillis());

        rechangeVo.setRechange_sn(tradeSnCreator.generateRechangeSn());



        iDaoSupport.insert("", rechangeVo);



        return rechangeVo;
    }
}
