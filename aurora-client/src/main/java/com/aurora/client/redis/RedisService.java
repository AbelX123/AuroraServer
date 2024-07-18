package com.aurora.client.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis业务流程通用服务
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 存储
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 存储（设置过期时间）
     */
    public void setExpMillis(String key, Object value, Long exp) {
        redisTemplate.opsForValue().set(key, value, exp, TimeUnit.MILLISECONDS);
    }

    /**
     * 批量存储
     */
    public void multiSet(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    /**
     * 获取
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量删除
     */
    public Long deleteBatch(String... keys) {
        return redisTemplate.delete(new HashSet<>(Arrays.asList(keys)));
    }

    /**
     * 事务操作
     *
     * @param sessionCallback 事务执行的回调函数
     */
    public Object executeTransaction(SessionCallback<Object> sessionCallback) {
        return redisTemplate.execute(sessionCallback);
    }
}
