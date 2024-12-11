package com.gio.couponsystem.conpon.controller;

import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.conpon.dto.CouponCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


@Tag(name = "선착순 쿠폰 API", description = "선착순 쿠폰 API")
public interface CouponSwaggerController {

    @Operation(summary = "선착순 쿠폰 생성")
    @ApiResponse(responseCode = "201", description = "선착순 쿠폰 생성 성공")
    ResponseEntity<CouponCreateResponse> createCoupon(CouponCreateRequest request);


    @Operation(summary = "선착순 쿠폰 조회")
    @ApiResponse(responseCode = "200", description = "선착순 쿠폰 조회 성공")
    ResponseEntity<CouponCreateResponse> getCoupon(@PathVariable Long couponId);
}
