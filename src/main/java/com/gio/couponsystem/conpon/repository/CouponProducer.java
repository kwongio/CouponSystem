package com.gio.couponsystem.conpon.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String COUPON_ASSIGN_TOPIC = "coupon-assign";
    private final ObjectMapper objectMapper;

    public void sendAssignCouponRequest(CouponAssignRequest request) {
        try {
            String req = objectMapper.writeValueAsString(request);
            log.info("Sending assign coupon request: {}", req);
            kafkaTemplate.send(COUPON_ASSIGN_TOPIC, req);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
