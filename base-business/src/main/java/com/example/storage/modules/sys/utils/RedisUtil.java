package com.example.storage.modules.sys.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
@Slf4j
public class RedisUtil {
	@Autowired
	private JedisPool jedisPool;
	
	/**
	 * 通过key获取储存在redis中的value
	 * @param key
	 * @param indexdb 选择redis库 0-15
	 * @return 成功返回value 失败返回null
	 */
	public Object get(String key, int indexdb) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(indexdb);
			value = jedis.get(key);
		} catch (Exception e) {
			log.error("redis连接池异常 ", e);
		} finally {
			returnResource(jedisPool, jedis);
		}
		return value;
	}
	/**
	 * 向redis存入key和value
	 * 
	 * 如果key已存在，则覆盖
	 * @param key
	 * @param value
	 * @param indexdb 选择redis库 0-15
	 * @return 成功返回ok 失败返回null
	 */
	public String set(String key, String value, int indexdb) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(indexdb);
			return jedis.set(key, value);
		} catch (Exception e) {
			log.error("redis连接池异常 ", e);
			return "0";
		} finally {
			returnResource(jedisPool, jedis);
		}
	}
	/**
	 * 向redis存入key和value,并设置过期时间
	* 
	 * 如果key已存在，则覆盖
	 * @param key
	 * @param value
	 * @param indexdb 选择redis库 0-15
	 * @return 成功返回ok 失败返回null
	 */
	public String set(String key, String value, Integer seconds, int indexdb) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(indexdb);
			return jedis.setex(key, seconds, value);
		} catch (Exception e) {
			log.error("redis连接池异常 ", e);
			return "0";
		} finally {
			returnResource(jedisPool, jedis);
		}
	}
	/**
	 * 根据key 删除缓存中的对象
	 * @param key
	 * @param indexdb indexdb 选择redis库 0-15
	 * @return 成功返回long,失败返回0
	 */
	public long del(String key, int indexdb) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.select(indexdb);
            long count=jedis.del(key);
            return count;
        } catch (Exception e) {
        	log.error("redis连接池异常 ", e);
			return 0;
        } finally {
        	returnResource(jedisPool, jedis);
        }
    }
	/**
	 * 清空当前数据库中的所有key，此命令从不失败
	 * @return
	 */
	public String flushDB() {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			return jedis.flushDB();
		} catch (Exception e) {
			log.error("redis连接池异常 ", e);
		} finally {
			returnResource(jedisPool, jedis);
		}
		return null;
	}
    /**
     * 判断key是否存在
     * @param key
     * @return 存在返回true 不存在返回false
     */
	public boolean exists(String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
        	log.error("redis连接池异常 ", e);
			return false;
        }finally {
        	returnResource(jedisPool, jedis);
        }
    }
	/**
	 * 在indexdb库，判断key是否存在
	 * @param key
	 * @param indexdb
	 * @return 存在返回true 不存在返回false
	 */
	public Boolean exists(String key,int indexdb){
		Jedis jedis = null;
		try {
		    jedis = jedisPool.getResource();
		    jedis.select(indexdb);
		    return jedis.exists(key);
		} catch (Exception e) {
			log.error("redis连接池异常 ", e);
			return false;
		}finally {
			returnResource(jedisPool, jedis);
		}
	}
	/**
	 * 以秒为单位，返回给定 key 的剩余生存时间
	 * @param key
	 * @param indexdb
	 * @return 当 key 不存在时，返回 -2 ，当 key 存在但没有设置剩余生存时间时，返回 -1 ，否则 以秒为单位，返回 key的剩余生存时间， 发生异常 返回 0
	 * 
	 */
	public Long ttl(String key, int indexdb) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(indexdb);
			return jedis.ttl(key);
		} catch (Exception e) {
			log.error("redis连接池异常 ", e);
			return 0L;
		} finally {
			returnResource(jedisPool, jedis);
		}
	}
	/**
	 * 返回到连接池
	 * @param jedisPool
	 * @param jedis
	 */
	public static void returnResource(JedisPool jedisPool, Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}

}
