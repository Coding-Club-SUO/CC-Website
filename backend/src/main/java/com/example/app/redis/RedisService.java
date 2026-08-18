/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.app.redis;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author rashi
 */
@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(String key, String value, long ttlSeconds) {
        Duration exp = Duration.ofSeconds(ttlSeconds);
        redisTemplate.opsForValue().set(key, value, exp);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    
    public StringRedisTemplate getRedis() {
        return redisTemplate;
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}