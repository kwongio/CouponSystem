package com.gio.couponsystem.config;

import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = Map.of("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic couponAssignTopic() {
        return TopicBuilder.name("coupon-assign")
                .partitions(3)
                .replicas(3)
                .build();
    }
}
