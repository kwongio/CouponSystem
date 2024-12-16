package com.gio.couponsystem.conpon.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@NoArgsConstructor
@Entity
public class CouponAssignLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("할당 기록 고유 ID")
    private Long id;

    @Column(nullable = false, columnDefinition = "BIGINT NOT NULL")
    @Comment("쿠폰 ID")
    private Long couponId;

    @Column(nullable = false, columnDefinition = "CHAR(36) NOT NULL", unique = true)
    @Comment("쿠폰 할당 고유 값(UUID)")
    private String uuid;

    @Builder
    public CouponAssignLog(Long id, Long couponId, String uuid) {
        this.id = id;
        this.couponId = couponId;
        this.uuid = uuid;
    }
}
