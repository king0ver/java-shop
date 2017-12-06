package com.enation.eop.sdk.config.redis.transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.enation.framework.util.StringUtil;
 
 

/***
 * Redis事务切面控制
 * @author kingapex
 * @version 1.0
 * @since pangu1.0
 * 2017年6月9日上午10:13:27
 */
@Aspect
@Component
public class RedisTransactionalAspect {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());  
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 获取锁的最长等待时间
	 */
	@Value("${redis.transactional.acquireTimeout:10}"  )
	private Integer _acquireTimeout;
	
	/**
	 * 锁的超时时间
	 */
	@Value("${redis.transactional.lockTimeout:3}")
	private Integer _lockTimeout;
	
	
	@Around(value = "@annotation(transactional)")
	public Object aroundMethod(ProceedingJoinPoint pjd, RedisTransactional transactional) throws Throwable {
		Object result = null;
		
		//生成 lock key name
		CodeSignature signature = (CodeSignature)pjd.getSignature();
		String longName = signature.toLongString();
		 
		 
		 //初始化默认值
//		 _acquireTimeout=_acquireTimeout==null?10:_acquireTimeout;
//		 _lockTimeout=_lockTimeout==null?5:_lockTimeout;
		 
		 
		String identifier = this.aquirePessimisticLockWithTimeout(longName, _acquireTimeout, _lockTimeout);
		try {

			if (!StringUtil.isEmpty(identifier)) {
				result = pjd.proceed();
				return result;
			}else{
				if( logger.isErrorEnabled()){
					this.logger.error("redis事务获取锁失败"); 
				}
				throw new RuntimeException("redis事务获取锁失败");
			}

		} catch (Throwable e) {
			if( logger.isErrorEnabled()){
				this.logger.error("redis事务失败",e); 
			}
			throw new RuntimeException(e);
		} finally {
			releasePessimisticLockWithTimeout(longName, identifier);
		}
	}

	
	/**
	 * 获取锁
	 * @param lockName
	 * @param acquireTimeout
	 * @param lockTimeout
	 * @return
	 */
	public String aquirePessimisticLockWithTimeout(String lockName, int acquireTimeout, int lockTimeout) {
		if (StringUtil.isEmpty(lockName) || lockTimeout <= 0)
			return null;
		final String lockKey = lockName;
		String identifier = UUID.randomUUID().toString();
		Calendar atoCal = Calendar.getInstance();
		atoCal.add(Calendar.SECOND, acquireTimeout);
		Date atoTime = atoCal.getTime();
		int i = 0;
		while (true) {
			if (i > 0){
				if( logger.isDebugEnabled()){
					this.logger.debug("第" + i + "次重试");
				}
			}
			// try to acquire the lock
			if (redisTemplate.execute(new RedisCallback<Boolean>() {
				@Override
				public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
					return connection.setNX(redisTemplate.getStringSerializer().serialize(lockKey),
							redisTemplate.getStringSerializer().serialize(identifier));
				}
			})) { // successfully acquired the lock, set expiration of the lock
				redisTemplate.execute(new RedisCallback<Boolean>() {
					@Override
					public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
						return connection.expire(redisTemplate.getStringSerializer().serialize(lockKey), lockTimeout);
					}
				});
				return identifier;
			} else { // fail to acquire the lock
				// set expiration of the lock in case ttl is not set yet.
				if (null == redisTemplate.execute(new RedisCallback<Long>() {
					@Override
					public Long doInRedis(RedisConnection connection) throws DataAccessException {
						return connection.ttl(redisTemplate.getStringSerializer().serialize(lockKey));
					}
				})) {
					// set expiration of the lock
					redisTemplate.execute(new RedisCallback<Boolean>() {
						@Override
						public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
							return connection.expire(redisTemplate.getStringSerializer().serialize(lockKey),
									lockTimeout);
						}
					});
				}
				if (acquireTimeout < 0) // no wait
					return null;
				else {
					try {
						Thread.sleep(100l); // wait 100 milliseconds before
											// retry
						i++;
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
				if (new Date().after(atoTime)) {
					break;
				}
			}
		}
		return null;
	}

	
	/**
	 * 释放锁
	 * @param lockName
	 * @param identifier
	 */
	public void releasePessimisticLockWithTimeout(String lockName, String identifier) {
		if (StringUtil.isEmpty(lockName) || StringUtil.isEmpty(identifier))
			return;
		final String lockKey = lockName;

		redisTemplate.execute(new RedisCallback<Void>() {
			@Override
			public Void doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] ctn = connection.get(redisTemplate.getStringSerializer().serialize(lockKey));
				if (ctn != null && identifier.equals(redisTemplate.getStringSerializer().deserialize(ctn)))
					connection.del(redisTemplate.getStringSerializer().serialize(lockKey));
				return null;
			}
		});
	}

}
