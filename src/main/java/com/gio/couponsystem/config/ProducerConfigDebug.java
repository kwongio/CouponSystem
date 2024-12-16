package com.gio.couponsystem.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProducerConfigDebug {
    private final ProducerFactory<?, ?> producerFactory;

    @PostConstruct
    public void printProducerConfig() {
        log.info("Producer config: {}", producerFactory.getConfigurationProperties());
    }
}
