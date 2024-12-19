package com.gio.couponsystem.config;

import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {
    public static final String COUPON_ASSIGN_TOPIC = "coupon-assign";
    public static final String COUPON_ASSIGN_DLT_TOPIC = "coupon-assign-dlt";
    public static final int DEFAULT_PARTITION_COUNT = 3;
    public static final int DEFAULT_REPLICA_COUNT = 3;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = Map.of(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProducerConfig.DEFAULT_KAFKA_BROKER_SERVER);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic couponAssignTopic() {
        return TopicBuilder.name(COUPON_ASSIGN_TOPIC)
                .partitions(DEFAULT_PARTITION_COUNT)
                .replicas(DEFAULT_REPLICA_COUNT)
                .build();
    }

    @Bean
    public NewTopic dlt() {
        return TopicBuilder.name(COUPON_ASSIGN_DLT_TOPIC)
                .partitions(DEFAULT_PARTITION_COUNT)
                .replicas(DEFAULT_REPLICA_COUNT)
                .build();
    }
}
