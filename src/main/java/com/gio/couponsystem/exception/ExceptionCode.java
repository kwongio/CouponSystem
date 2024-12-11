package com.gio.couponsystem.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ExceptionCode {

    COUPON_NOT_FOUND("Coupon-0001", HttpStatus.NOT_FOUND, "해당 쿠폰이 존재하지 않습니다."),
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
