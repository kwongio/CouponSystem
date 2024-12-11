package com.gio.couponsystem.conpon.controller;

import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.conpon.dto.CouponCreateResponse;
import com.gio.couponsystem.conpon.dto.CouponQueryResponse;
import com.gio.couponsystem.conpon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController implements CouponSwaggerController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponCreateResponse> createCoupon(@Valid @RequestBody CouponCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CouponCreateResponse.from(couponService.create(request)));
    }

    @GetMapping("/{couponId}")
    public ResponseEntity<CouponCreateResponse> getCoupon(@PathVariable("couponId") Long couponId) {
        return ResponseEntity.ok(CouponQueryResponse.from(couponService.getCoupon(couponId)));
    }
}
