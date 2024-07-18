package com.aurora.client.config;

import com.aurora.client.redis.FastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashSet;
import java.util.Set;

/**
 * Redis配置
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 允许反序列化的类
        Set<String> allowedClass = new HashSet<>();
        allowedClass.add("com.aurora.client.common.vo.UserVO");

        // StringRedisSerializer序列化和反序列化redis的key
        template.setKeySerializer(new StringRedisSerializer());

        // Fastjson2RedisSerializer序列化和反序列化redis的value
        FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class, allowedClass);
        template.setValueSerializer(fastJsonRedisSerializer);

        // StringRedisSerializer序列化和反序列化hash的key
        template.setHashKeySerializer(new StringRedisSerializer());

        // Fastjson2RedisSerializer序列化和反序列化hash的value
        template.setHashValueSerializer(fastJsonRedisSerializer);

        // 事务支持
        template.setEnableTransactionSupport(true);

        template.afterPropertiesSet();

        return template;
    }

}
