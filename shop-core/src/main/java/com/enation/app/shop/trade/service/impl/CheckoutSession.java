package com.enation.app.shop.trade.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.enation.app.shop.trade.model.vo.PaymentType;
import com.enation.app.shop.trade.model.vo.Receipt;
import com.enation.app.shop.trade.service.ICheckoutSession;
import com.enation.app.shop.trade.support.CheckoutParam;
import com.enation.app.shop.trade.support.CheckoutParamName;
import com.enation.eop.sdk.context.UserConext;

/**
 * 结算session业务类<br>
 * 在创建订单时锁定session
 * 
 * @author kingapex
 * @version 1.0
 * @since pangu1.0 2017年3月18日上午10:42:32
 */
@Service
public class CheckoutSession implements ICheckoutSession {

	@Autowired
	private RedisTemplate redisTemplate;

	public CheckoutSession() {
		
	}

	public void finalize() throws Throwable {

	}

	private String getRedisKey(){
		Integer memberid = UserConext.getCurrentMember().getMember_id();
		return  "CHECKOUT_PARAM_"+memberid;
	}
	
	
	/**
	 * 由reids中读取出参数
	 */
	public CheckoutParam read() {
		String key = getRedisKey();
		Map<String,Object> map = this.redisTemplate.opsForHash().entries(key);
		
		//如果还没有存过则返回null
		if (map == null || map.isEmpty()) {
			return null;
		}
		
		
		//如果取到了，则取出来生成param
		Integer addressId  = (Integer)map.get(CheckoutParamName.ADDRESSID);
		PaymentType paymentType = (PaymentType)map.get(CheckoutParamName.PAYMENTTYPE);
		Receipt receipt  = (Receipt)map.get(CheckoutParamName.RECEIPT);
		String receiveTime  = (String)map.get(CheckoutParamName.RECEIVETIME);
		String remark  = (String)map.get(CheckoutParamName.REMART);
		String clientType = (String) map.get(CheckoutParamName.CLIENTTYPE);
		
		
		CheckoutParam param = new CheckoutParam();
		
		param.setAddressId(addressId);
		param.setReceipt(receipt);
		param.setReceive_time(receiveTime);
		param.setRemark(remark);
		if(paymentType == null){
			paymentType = PaymentType.defaultType();
		}
		param.setPaymentType(paymentType);
		param.setClient_type(clientType);
		
		return param;
	}

	
	/**
	 * 写入值
	 * 首先要检查此session是否已经上锁
	 */
	public void write(String key, Object value) {
		String redisKey = getRedisKey();
		//没有上锁则写入
		if( !checkLock(  )){
			this.redisTemplate.opsForHash().put(redisKey, key, value);
		}
		
	}
	
	
	/**
	 * 将全部参数压入
	 */
	@Override
	public void write(CheckoutParam param) {
		
		//没有上锁则写入
		if( !checkLock(  )){
			String redisKey = getRedisKey();
			Map map  = new HashMap<>(4);
			
			if(param.getAddressId()!= null){
				map.put(CheckoutParamName.ADDRESSID, param.getAddressId());
			}
			
			if(param.getReceive_time()!= null){
				map.put(CheckoutParamName.RECEIVETIME, param.getReceive_time());
			}
			
			if(param.getPaymentType()!= null){
				map.put(CheckoutParamName.PAYMENTTYPE, param.getPaymentType());
			}
			if(param.getReceipt()!= null){
				map.put(CheckoutParamName.RECEIPT, param.getReceipt());
			}
			if(param.getRemark()!= null){
				map.put(CheckoutParamName.REMART, param.getRemark());
			}
			if(param.getClient_type()!= null){
				map.put(CheckoutParamName.CLIENTTYPE, param.getClient_type());
			}
			this.redisTemplate.opsForHash().putAll(redisKey ,map );
		}
		
		        
	}
 
	/**
	 * session锁前缀
	 */
	private static String LOCK_KEY_PRE ="check_param_lock_";
	
	/**
	 * 获取session锁在redis中的key
	 * @param sessionid
	 * @return
	 */
	private String getLockKey( ){
		
		//TODO 此处需要调用会员接口
		Integer memberid = UserConext.getCurrentMember().getMember_id();
		return  LOCK_KEY_PRE+memberid;
		
	}
	
	/**
	 * 检测此session是否已经上锁
	 * @param sessionid
	 * @return
	 */
	@Override
	public boolean checkLock( ) {
		
//		String lock= (String)  this.redisTemplate.opsForValue().get(this.getLockKey( ));
//		return "yes".equals(lock);
		return false;
	}

	@Override
	public void lock( ) {
		
		this.redisTemplate.opsForValue().set(this.getLockKey( ), "yes");
	}

	@Override
	public void unlock( ) {
		
		this.redisTemplate.delete(this.getLockKey( ));
	}



}