package com.gio.couponsystem.conpon.repository;

import com.gio.couponsystem.conpon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
