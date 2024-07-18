package com.aurora.client.redis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class FastJsonRedisSerializer<T> implements RedisSerializer<T> {

    private final Class<T> clazz;

    private final JSONReader.AutoTypeBeforeHandler autoTypeBeforeHandler;

    public FastJsonRedisSerializer(Class<T> clazz, Set<String> allowedClasses) {
        this.clazz = clazz;
        this.autoTypeBeforeHandler = JSONReader.autoTypeFilter(allowedClasses.toArray(new String[0]));
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return JSON.toJSONString(t, JSONWriter.Feature.WriteClassName).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        String str = new String(bytes, StandardCharsets.UTF_8);
        return JSON.parseObject(str, clazz, autoTypeBeforeHandler);
    }
}
