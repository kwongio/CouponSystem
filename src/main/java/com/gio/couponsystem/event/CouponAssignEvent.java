package com.gio.couponsystem.event;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponAssignEvent {
    private Long couponId;
    private UUID uuid;
}
