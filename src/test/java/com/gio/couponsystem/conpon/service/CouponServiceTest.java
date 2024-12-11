package com.gio.couponsystem.conpon.service;

import com.gio.couponsystem.conpon.domain.Coupon;
import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.exception.CustomException;
import com.gio.couponsystem.exception.ExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @DisplayName("쿠폰 생성 테스트")
    @Test
    void create() {
        // Given
        CouponCreateRequest request = new CouponCreateRequest(
                "Discount Coupon",
                1000L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );

        // When
        Coupon coupon = couponService.create(request);

        // Then
        assertThat(coupon).isNotNull();
        assertThat(coupon.getTitle()).isEqualTo("Discount Coupon");
        assertThat(coupon.getQuantity()).isEqualTo(1000L);
        assertThat(coupon.getStartDate()).isEqualTo(request.getStartDate());
        assertThat(coupon.getEndDate()).isEqualTo(request.getEndDate());
    }

    @DisplayName("존재하지 않는 쿠폰 조회 시 예외 발생")
    @Test
    void getCoupon_notFound_shouldThrowException() {
        // Given
        Long invalidCouponId = 999L;

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.getCoupon(invalidCouponId);
        });

        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.COUPON_NOT_FOUND);
    }

    @DisplayName("쿠폰 조회 테스트")
    @Test
    void getCoupon() {
        // Given
        CouponCreateRequest request = new CouponCreateRequest(
                "Test Coupon",
                500L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );
        Coupon createdCoupon = couponService.create(request);

        // When
        Coupon retrievedCoupon = couponService.getCoupon(createdCoupon.getId());

        // Then
        assertThat(retrievedCoupon).isNotNull();
        assertThat(retrievedCoupon.getId()).isEqualTo(createdCoupon.getId());
        assertThat(retrievedCoupon.getTitle()).isEqualTo("Test Coupon");
        assertThat(retrievedCoupon.getQuantity()).isEqualTo(500L);
    }

    @DisplayName("쿠폰 수량이 음수인 경우 예외 발생")
    @Test
    void create_withNegativeQuantity_shouldThrowException() {
        // Given
        CouponCreateRequest request = new CouponCreateRequest(
                "Negative Quantity Coupon",
                -10L, // Invalid quantity
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(request);
        });

        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.COUPON_INVALID_QUANTITY);
    }

    @DisplayName("종료일이 시작일보다 이전인 경우 예외 발생")
    @Test
    void create_withInvalidDates_shouldThrowException() {
        // Given
        CouponCreateRequest request = new CouponCreateRequest(
                "Invalid Dates Coupon",
                100L,
                LocalDateTime.now().plusDays(10),
                LocalDateTime.now().plusDays(1)
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(request);
        });

        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.COUPON_INVALID_DATE);
    }

    @DisplayName("쿠폰 제목이 비어 있는 경우 예외 발생")
    @Test
    void create_withEmptyTitle_shouldThrowException() {
        // Given
        CouponCreateRequest request = new CouponCreateRequest(
                "",
                100L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(request);
        });

        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.COUPON_EMPTY_TITLE);
    }

    @DisplayName("쿠폰 제목이 너무 긴 경우 예외 발생")
    @Test
    void create_withTooLongTitle_shouldThrowException() {
        // Given
        String longTitle = "A".repeat(101);
        CouponCreateRequest request = new CouponCreateRequest(
                longTitle,
                100L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            couponService.create(request);
        });

        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.COUPON_INVALID_TITLE);
    }

}
