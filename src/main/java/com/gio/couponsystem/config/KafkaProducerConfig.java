package com.gio.couponsystem.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public DefaultKafkaProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        // Kafka broker 정보
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");

        // 직렬화 설정
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 데이터 유실 방지 설정
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // 멱등성 활성화
        configProps.put(ProducerConfig.ACKS_CONFIG, "all"); // 모든 복제 완료 후 확인

        // 배치 설정
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 100); // 최대 100ms 대기
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 1000000); // 최대 1MB 배치 크기

        // 재시도 및 타임아웃 설정
        configProps.put(ProducerConfig.RETRIES_CONFIG, 10); // 실패 시 최대 5번 재시도
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000); // 요청 타임아웃 10초

        // 압축 설정 (선택 사항)
//        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "gzip"); // gzip으로 압축

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
