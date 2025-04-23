package com.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderConsumer {

    @KafkaListener(
      topics = "order-test-topic",
      groupId = "order-group",
      containerFactory = "stringListenerFactory" // ✅ 명시
    )
    public void consume(String message) {
        log.info("🧾 메시지 수신: {}", message);
    }
}
