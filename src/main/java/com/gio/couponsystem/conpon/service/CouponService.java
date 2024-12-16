package com.gio.couponsystem.conpon.service;

import com.gio.couponsystem.conpon.domain.Coupon;
import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.conpon.repository.CouponAssignRequest;
import com.gio.couponsystem.conpon.repository.CouponProducer;
import com.gio.couponsystem.conpon.repository.CouponRepository;
import com.gio.couponsystem.conpon.validator.CouponValidator;
import com.gio.couponsystem.exception.CustomException;
import com.gio.couponsystem.exception.ExceptionCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;
    private final CouponProducer couponProducer;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Coupon create(CouponCreateRequest request) {
        Coupon coupon = request.toEntity();
        couponValidator.validate(coupon);
        return couponRepository.save(coupon);
    }

    public Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.COUPON_NOT_FOUND));
    }

    public void assignCoupon(Long couponId) {
        Long decrement = redisTemplate.opsForValue().decrement("coupon:" + couponId);
        log.info("remain coupon: {}", decrement);
        if (decrement != null && decrement >= 0) {
            couponProducer.sendAssignCouponRequest(new CouponAssignRequest(couponId, UUID.randomUUID()));
        } else {
            throw new CustomException(ExceptionCode.COUPON_OUT_OF_STOCK);
        }
    }
}
