package com.gio.couponsystem.conpon.dto;

import com.gio.couponsystem.conpon.domain.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "쿠폰 생성 응답 데이터") // 클래스 수준에서 설명 추가
public record CouponCreateResponse(
        @Schema(description = "쿠폰 ID", example = "1")
        Long id,

        @Schema(description = "쿠폰 제목", example = "10% 할인 쿠폰")
        String title,

        @Schema(description = "쿠폰 수량", example = "100")
        long quantity,

        @Schema(description = "쿠폰 시작 날짜", example = "2023-12-01T00:00:00")
        LocalDateTime startDate,

        @Schema(description = "쿠폰 종료 날짜", example = "2023-12-31T23:59:59")
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
