package com.gio.couponsystem.exception;

import lombok.Builder;

@Builder
public record ServerExceptionResponse(
        int status,       // HTTP 상태 코드
        String error,     // 오류 유형
        String message,   // 오류 메시지
        long timestamp    // 발생 시각
) {}
