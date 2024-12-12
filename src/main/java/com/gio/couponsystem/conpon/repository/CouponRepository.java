package com.gio.couponsystem.conpon.repository;

import com.gio.couponsystem.conpon.domain.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where c.id = :id")
    Optional<Coupon> findByIdWithLock(@Param("id") Long id);


    @Modifying
    @Query("UPDATE Coupon c SET c.quantity = c.quantity - 1 WHERE c.id = :couponId AND c.quantity > 0")
    int decrementCouponQuantity(@Param("couponId") Long couponId);
}
