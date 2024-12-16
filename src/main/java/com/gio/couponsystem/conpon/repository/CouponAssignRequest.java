package com.gio.couponsystem.conpon.repository;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponAssignRequest {
    private Long couponId;
    private UUID uuid;
}
