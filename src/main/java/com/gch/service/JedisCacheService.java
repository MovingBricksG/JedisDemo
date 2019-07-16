package com.gch.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JedisCacheService {
	@Autowired //自动注入redis连接池
    private JedisPool jedisPool;

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           this.close(jedis); 
        }

    }

    public void setWithExpire(String key, String value, Integer times) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, times);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
    }

    public boolean existKey(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.exists(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return false;
    }
    
    public String getHash(String key, String field) {
    	Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String hget = jedis.hget(key, field);
            return hget;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return null;
    }
    
    public void setHash(String key, Map<String, String> map) {
    	Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hmset(key, map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
    }
    
    public void del(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(key);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
    }
    
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String s = jedis.get(key);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(jedis);
        }
        return null;
    }

    public void close(Jedis jedis){
        if (jedis != null) {
            jedis.close();
            if (jedis.isConnected()) {
                try {
                    jedis.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
