package com.gio.couponsystem.conpon.service;

import com.gio.couponsystem.conpon.domain.Coupon;
import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.exception.CustomException;
import com.gio.couponsystem.exception.ExceptionCode;
import com.gio.couponsystem.redis.RedisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private RedisRepository redisRepository;


    @Disabled("실패 테스트를 위해 비활성화")
    @DisplayName("쿠폰 할당  테스트")
    @Test
    void assign() {
        // Given
        redisRepository.save("coupon:1", "20000");
        int initialQuantity = redisRepository.get("coupon:1");
        Long couponId = 1L;

        // When
        couponService.assignCoupon(couponId);

        // Then
        int remainCouponCount = redisRepository.get("coupon:1");
        assertThat(remainCouponCount).isEqualTo(initialQuantity - 1);
    }

    @Disabled("실패 테스트를 위해 비활성화")
    @DisplayName("쿠폰 할당 동시성 테스트")
    @Test
    void assignRaceConditionTest() throws InterruptedException {
        // Given
        long couponId = 1L;
        redisRepository.save("coupon:1", "100");

        // When
        int numberOfThreads = 100; // 동시에 실행할 스레드 수
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads); // 모든 스레드가 준비된 후 시작

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    couponService.assignCoupon(couponId); // 쿠폰 할당 메서드 호출
                } catch (Exception e) {
                    // 예외 발생 시 로그 출력
                    System.err.println("Error during coupon assignment: " + e.getMessage());
                } finally {
                    latch.countDown(); // 스레드 작업 완료 시 카운트 감소
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // Then
        int remainCouponCount = redisRepository.get("coupon:1");
        assertThat(remainCouponCount).isEqualTo(0); // 남은 수량이 음수가 되지 않는지 확인
    }


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
