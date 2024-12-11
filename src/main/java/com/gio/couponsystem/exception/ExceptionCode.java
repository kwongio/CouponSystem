package com.gio.couponsystem.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ExceptionCode {

    COUPON_NOT_FOUND("Coupon-0001", HttpStatus.NOT_FOUND, "해당 쿠폰이 존재하지 않습니다."),
    COUPON_INVALID_QUANTITY("Coupon-0002", HttpStatus.BAD_REQUEST, "쿠폰은 1개 이상 생성해야 합니다."),
    COUPON_INVALID_DATE("Coupon-0003", HttpStatus.BAD_REQUEST, "만료일은 시작일보다 빠를 수 없습니다."),
    COUPON_INVALID_TITLE("Coupon-0004", HttpStatus.BAD_REQUEST, "쿠폰 제목은 100자 이하로 입력해야 합니다."),
    COUPON_EMPTY_TITLE("Coupon-0005", HttpStatus.BAD_REQUEST, "쿠폰 제목은 1자 이상 입력해야 합니다."),
    ;
    private final String code;
    private final HttpStatus status;
    private final String message;

    ExceptionCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }
}
