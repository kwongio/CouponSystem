package com.gio.couponsystem.conpon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gio.couponsystem.conpon.dto.CouponCreateRequest;
import com.gio.couponsystem.conpon.service.CouponService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CouponControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponService couponService;


    @DisplayName("쿠폰 생성 테스트")
    @Test
    void createCoupon() throws Exception {
        // Given
        CouponCreateRequest request = new CouponCreateRequest(
                "Discount Coupon",
                1000L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );

        // When & Then
        mockMvc.perform(post("/coupons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Discount Coupon")))
                .andExpect(jsonPath("$.quantity", is(1000)));
    }

    @DisplayName("쿠폰 조회 테스트")
    @Test
    void getCoupon() throws Exception {
        // Given
        Long couponId = 1L;
        CouponCreateRequest request = new CouponCreateRequest(
                "Discount Coupon",
                1000L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(10)
        );
        couponService.create(request);

        // When & Then
        mockMvc.perform(get("/coupons/{couponId}", couponId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Discount Coupon")))
                .andExpect(jsonPath("$.quantity", is(1000)));
    }
}
