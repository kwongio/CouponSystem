package com.gio.couponsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class CouponConfig {
    public static final String COUPON_COUNT = "20000";
    public static final String COUPON_KEY = "coupon:1";
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            redisTemplate.opsForValue().set(COUPON_KEY, COUPON_COUNT);
        };
    }
}
