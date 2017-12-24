package com.enation.app.shop.trade.service.impl;

import com.enation.app.shop.trade.service.ITradeSnCreator;
import com.enation.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 1、通过Redis的自增来控制编号的自增
 * 2、编号规则为：年月日6位自增编码(容纳日单10万）
 * 3、通地任务调度每天清除前一天的redis编码key
 * Created by kingapex on 2017/10/16.
 *
 * @author kingapex
 * @version 1.0
 * @since 6.4.0
 * 2017/10/16
 */
@Service
public class TradeSnCreatorImpl implements ITradeSnCreator {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //交易编号缓存key前缀
    private  static  final  String TRADE_SN_CACHE_PREFIX="TRADE_SN";

    //订单编号缓存key前缀
    private  static  final  String ORDER_SN_CACHE_REFIX="ORDER_SN";

    //订单编号缓存key前缀
    private  static  final  String RECHNAGE_SN_CACHE_REFIX="RECHANGE_SN";


    /**
     * 生成交易编号
     * @return 交易编号，格式如：20171022000011
     */
    @Override
    public String generateTradeSn() {

        String key = TRADE_SN_CACHE_PREFIX;

        String sn = generateSn(key);

        return sn;
    }

    /**
     * 生成订单编号
     * @return 订单编号，格式如：20171022000011
     */
    @Override
    public String generateOrderSn() {

        String key = ORDER_SN_CACHE_REFIX;

        String sn = generateSn(key);

        return sn;
    }



    @Override
    public void cleanCache() {
        Date yesterday = getYesterday( );
        String timeStr = DateUtil.toString(yesterday,"yyyyMMdd");
        stringRedisTemplate.delete(TRADE_SN_CACHE_PREFIX+"_"+timeStr);
        stringRedisTemplate.delete(ORDER_SN_CACHE_REFIX+"_"+timeStr);
    }

    @Override
    public String generateRechangeSn() {

        String key = RECHNAGE_SN_CACHE_REFIX;

        String sn = generateSn(key);

        return sn;
    }


    private  Date getYesterday(   ){
        Calendar cal = Calendar.getInstance();
       // cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        return  cal.getTime();
    }


    /**
     * 通过Redis的自增来控制编号的自增
     * @param key 区分类型的主key，日期会连在这个key后面
     * @return 生成的编码
     */
    private  String generateSn(String key){

        String timeStr = DateUtil.toString(new Date(),"yyyyMMdd");//+"15";

        //组合出当天的Key
        String redisKey = key+"_"+timeStr;

        //用当天的时间进行自增
        Long sncount=stringRedisTemplate.opsForValue().increment(redisKey, 1);


        String sn ;

        if( sncount <1000000){
            sn  = "000000" + sncount;
            sn = sn.substring(sn.length()-6,sn.length());
        }else{
            sn = String.valueOf(sncount);
        }

        sn=timeStr+sn;

        return sn;
    }





    public static void main(String[] args) {

        Date d =  DateUtil.toDate("2017-03-01 23:59","yyyy-MM-dd HH:mm");

        Calendar cal = Calendar.getInstance();

        // cal.setTime( d);
        cal.add(Calendar.DATE, -1);

        // long yestoday = d.getTime()-( 60 *60 *24 *1000 );
        // d = new Date(yestoday);
        System.out.println(  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(cal.getTime() ) );
    }
}
