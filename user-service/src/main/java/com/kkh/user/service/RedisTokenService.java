package com.kkh.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    private final StringRedisTemplate  redisTemplate;

    public void saveAccessToken(String username, String token) {
        redisTemplate.opsForValue().set("ACCESS_TOKEN:" + username, token, accessExpiration, TimeUnit.MILLISECONDS);
    }
    public void saveRefreshToken(String username, String token) {
        redisTemplate.opsForValue().set("REFRESH_TOKEN:" + username, token, refreshExpiration, TimeUnit.MILLISECONDS);
    }
}
