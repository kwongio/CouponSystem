package com.gio.couponsystem.exception;

import lombok.Builder;

import java.util.Map;

@Builder
public record ValidationExceptionResponse(
        int status,              // HTTP 상태 코드
        String error,            // 오류 유형
        String message,          // 오류 메시지
        long timestamp,          // 발생 시각
        Map<String, String> validationErrors // 필드별 상세 검증 오류
) { }
