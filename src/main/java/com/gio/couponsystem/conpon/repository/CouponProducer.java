package com.gio.couponsystem.conpon.repository;

import com.gio.couponsystem.conpon.converter.ObjectToStringConverter;
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
    private final ObjectToStringConverter objectToStringConverter;

    public void sendAssignCouponRequest(CouponAssignRequest request) {
        kafkaTemplate.send(COUPON_ASSIGN_TOPIC, objectToStringConverter.convert(request));
    }
}
