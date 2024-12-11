package com.gio.couponsystem.conpon.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("쿠폰 고유 ID")
    private Long id;

    @Column(nullable = false, length = 100, columnDefinition = "VARCHAR(100) NOT NULL")
    @Comment("쿠폰 제목 (최대 100자)")
    private String title;

    @Column(nullable = false, columnDefinition = "BIGINT CHECK (quantity >= 0 AND quantity <= 1000000000)")
    @Comment("쿠폰 수량 (0 이상 1,000,000,000 이하)")
    private long quantity;

    @Column(nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
    @Comment("쿠폰 유효 시작일")
    private LocalDateTime startDate;

    @Column(nullable = false, columnDefinition = "TIMESTAMP NOT NULL")
    @Comment("쿠폰 유효 종료일")
    private LocalDateTime endDate;

    @Comment("쿠폰 생성일")
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Coupon(String title, long quantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
