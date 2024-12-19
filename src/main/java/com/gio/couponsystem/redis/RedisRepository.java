package com.gio.couponsystem.redis;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public int get(String key) {
        return Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get(key)));
    }
}
