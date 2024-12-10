package com.gio.couponsystem.conpon.dto;

import com.gio.couponsystem.conpon.domain.Coupon;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    @Length(min = 1, max = 100, message = "제목은 1자 이상 100자 이하로 입력해주세요.")
    private String title;

    @Min(value = 0, message = "수량은 0 이상이어야 합니다.")
    @Max(value = 1_000_000, message = "수량은 1,000,000개 이하여야 합니다.")
    private long quantity;

    @NotNull(message = "시작일은 필수입니다.")
    @Future(message = "시작일은 현재 시간 이후여야 합니다.")
    private LocalDateTime startDate;


    @NotNull(message = "종료일은 필수입니다.")
    @Future(message = "종료일은 현재 시간 이후여야 합니다.")
    private LocalDateTime endDate;

    public CouponCreateRequest(String title, long quantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.title = title;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Coupon toEntity() {
        return Coupon.builder()
                .title(title)
                .quantity(quantity)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
}
