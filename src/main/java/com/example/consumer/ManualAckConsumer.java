package com.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ManualAckConsumer {

    @KafkaListener(
        topics = "manual-ack-topic",
        groupId = "manual-group",
        containerFactory = "manualAckListenerFactory"
    )
    public void listen(String message, Acknowledgment ack) {
        log.info("📨 메시지 수신: {}", message);

        try {
            if (message.contains("fail")) {
                throw new RuntimeException("강제 실패!");
            }

            // ✅ 처리 완료 → 커밋
            ack.acknowledge();
            log.info("✅ 메시지 커밋 완료");

        } catch (Exception e) {
            log.warn("❌ 예외 발생 → 커밋 안함, 재처리 예정: {}", message);
            // ack.acknowledge(); // ❌ 커밋하지 않음 → 메시지 다시 수신됨
        }
    }
}
