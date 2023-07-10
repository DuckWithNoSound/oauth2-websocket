package com.example.oauth2websocket.service.impl;

import com.example.oauth2websocket.service.IRedisService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class RedisService implements IRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void setValue(String key, String value, long expirationTime) {
        redisTemplate.opsForValue().set(key, value, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
