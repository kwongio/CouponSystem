package com.gio.couponsystem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@RequiredArgsConstructor
public class CouponConfig {
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            redisTemplate.opsForValue().set("coupon:1", "10000000");
        };
    }
}
