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

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;
    private final CouponValidator couponValidator;

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

    public synchronized void assignCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CustomException(ExceptionCode.COUPON_NOT_FOUND));
        coupon.assign();
        couponRepository.save(coupon);
    }
}
