package com.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConcurrencyConsumer {

    @KafkaListener(
      topics = "concurrency-test-topic",
      groupId = "concurrency-group",
      containerFactory = "concurrencyListenerFactory"
    )
    public void consume(String message) {
        log.info("💥 메시지 수신: {} | 처리 쓰레드: {}", message, Thread.currentThread().getName());
    }
}
