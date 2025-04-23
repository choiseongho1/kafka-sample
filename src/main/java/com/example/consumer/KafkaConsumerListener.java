package com.example.consumer;

import com.example.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumerListener {

    @KafkaListener(topics = "chat-topic", groupId = "json-group", containerFactory = "chatMessageListenerFactory")
    public void receive(ChatMessage message) {
        log.info("📩 JSON 메시지 수신: {}", message);
    }

    @KafkaListener(topics = "partition-test-topic", groupId = "partition-group")
    public void listenWithPartition(ConsumerRecord<String, String> record) {
        log.info("📦 파티션={}, 키={}, 메시지={}",
            record.partition(), record.key(), record.value());
    }

    @KafkaListener(
        topics = "user-order-count",
        groupId = "monitor-group",
        containerFactory = "stringListenerFactory" // ✅ 문자열로 받게 설정
    )
    public void listen(String message, @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.info("📡 실시간 누적 집계 수신 → userId={}, count={}", key, message);
    }
}