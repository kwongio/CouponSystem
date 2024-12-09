package com.gio.couponsystem.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private long quantity;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Builder
    public Coupon(String title, long quantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
