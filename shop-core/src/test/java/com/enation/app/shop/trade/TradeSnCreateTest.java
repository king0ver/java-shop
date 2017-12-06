package com.enation.app.shop.trade;

import com.enation.app.shop.trade.model.vo.Trade;
import com.enation.app.shop.trade.service.ITradeSnCreator;
import com.enation.framework.cache.ICache;
import com.enation.framework.test.SpringTestSupport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * 订单编号创建测试
 * Created by kingapex on 2017/10/16.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2017/10/16
         */
public class TradeSnCreateTest extends SpringTestSupport{


    @Autowired
    private ITradeSnCreator tradeSnCreator;

    @Autowired
    private ICache cache;


    /**
     * 获取失败交易列表
     */
    @Test
    public  void  test(){
        List<Trade> list    = (List<Trade>) cache.get("trade_fail_cache_key");
        for (Trade trade :list){
            System.out.println(trade.getTrade_sn());
        }
    }
    /**
     * 测试生成订单sn
     */
    @Test
    public  void createOrderSn(){

        for (int i=0;i<10;i++){
            String sn=  tradeSnCreator.generateOrderSn();
            System.out.println(sn);
        }

    }

    /**
     * 测试生成交易sn
     */
    @Test
    public  void createTradeSn(){

        for (int i=0;i<10;i++){
            String sn=  tradeSnCreator.generateTradeSn();
            System.out.println(sn);
        }

    }



    @Test
    public  void cleanTest(){
        tradeSnCreator.cleanCache();
    }
}
