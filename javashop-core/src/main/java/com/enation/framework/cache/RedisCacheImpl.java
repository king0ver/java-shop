package com.enation.framework.cache;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis的cache实现
 * 
 * @author fk
 * @version v6.4
 * @since v6.4 2017年9月25日 下午4:32:49
 */
public class RedisCacheImpl implements ICache {

	private RedisTemplate redisTemplate;
	
	public RedisCacheImpl(RedisTemplate redisTemplate) {
		
		this.redisTemplate = redisTemplate;
	}
	
	public RedisCacheImpl() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object get(Object key) {

		return redisTemplate.opsForValue().get(key);
	}

	@Override
	public void put(Object key, Object value) {

		redisTemplate.opsForValue().set(key, value);
	}

	@Override
	public void put(Object key, Object value, int exp) {
		
		redisTemplate.opsForValue().set(key, value,exp,TimeUnit.SECONDS);
	}

	@Override
	public void remove(Object key) {

		redisTemplate.delete(key);
	}

	@Override
	public void clear() {

		Set keys = redisTemplate.keys("*");
		redisTemplate.delete(keys);
	}

}
