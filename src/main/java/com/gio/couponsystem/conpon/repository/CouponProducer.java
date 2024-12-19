package com.gio.couponsystem.conpon.repository;

import com.gio.couponsystem.config.KafkaTopicConfig;
import com.gio.couponsystem.conpon.converter.ObjectToStringConverter;
import com.gio.couponsystem.event.CouponAssignEvent;
import io.micrometer.core.annotation.Counted;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectToStringConverter objectToStringConverter;

    @Counted(value = "coupon_assign")
    public void sendAssignCouponRequest(CouponAssignEvent request) {
        try {
            kafkaTemplate.send(KafkaTopicConfig.COUPON_ASSIGN_TOPIC, objectToStringConverter.convert(request));
        } catch (Exception e) {
            sendDLT(new CouponAssignEvent(request.getCouponId(), UUID.randomUUID()));
        }
    }

    private void sendDLT(CouponAssignEvent request) {
        kafkaTemplate.send(KafkaTopicConfig.COUPON_ASSIGN_DLT_TOPIC, objectToStringConverter.convert(request));
    }
}
