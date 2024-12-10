package com.gio.couponsystem.conpon.dto;

import com.gio.couponsystem.conpon.domain.Coupon;

import java.time.LocalDateTime;

public record CouponCreateResponse(
        Long id,
        String title,
        long quantity,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
    public static CouponCreateResponse from(Coupon coupon) {
        return new CouponCreateResponse(
                coupon.getId(),
                coupon.getTitle(),
                coupon.getQuantity(),
                coupon.getStartDate(),
                coupon.getEndDate()
        );
    }
}
