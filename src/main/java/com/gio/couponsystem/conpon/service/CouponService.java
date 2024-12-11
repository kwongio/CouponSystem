package com.gio.couponsystem.conpon.service;

import com.gio.couponsystem.conpon.domain.Coupon;
import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.conpon.repository.CouponRepository;
import com.gio.couponsystem.conpon.validator.CouponValidator;
import com.gio.couponsystem.exception.CustomException;
import com.gio.couponsystem.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;

    private final ConcurrentHashMap<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

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

    @Transactional
    public void assignCoupon(Long couponId) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.COUPON_NOT_FOUND));
        coupon.assign();
    }
}
