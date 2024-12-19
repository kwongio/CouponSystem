package com.gio.couponsystem.conpon.service;

import com.gio.couponsystem.conpon.domain.Coupon;
import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.event.CouponAssignEvent;
import com.gio.couponsystem.conpon.repository.CouponProducer;
import com.gio.couponsystem.conpon.repository.CouponRepository;
import com.gio.couponsystem.conpon.validator.CouponValidator;
import com.gio.couponsystem.exception.CustomException;
import com.gio.couponsystem.exception.ExceptionCode;
import com.gio.couponsystem.redis.RedisRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;
    private final CouponProducer couponProducer;
    private final RedisRepository redisRepository;

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
        Long remainCouponCount = redisRepository.decrement("coupon:" + couponId);
        log.info("Coupon {} stock: {}", couponId, remainCouponCount);
        if (remainCouponCount < 0) {
            throw new CustomException(ExceptionCode.COUPON_OUT_OF_STOCK);
        }
        throwTestException(remainCouponCount);
        couponProducer.sendAssignCouponRequest(new CouponAssignEvent(couponId, UUID.randomUUID()));
    }

    private static void throwTestException(Long remainCouponCount) {
        if (remainCouponCount % 2 == 0) {
            log.error("Test Exception remainCouponCount: {}", remainCouponCount);
            throw new RuntimeException("Test Exception");
        }
    }
}
