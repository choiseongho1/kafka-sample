package com.example.consumer;

import com.example.dto.RetryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RetryConsumer {

    @RetryableTopic(
        attempts = "3",
        backoff = @Backoff(delay = 2000),
        dltTopicSuffix = ".DLT",
        autoCreateTopics = "false"
    )
    @KafkaListener(topics = "retry-topic", groupId = "retry-group")
    public void listen(RetryMessage message) {
        log.info("🔁 메시지 수신: {}", message);

        if ("fail".equals(message.getContent())) {
            log.warn("❌ 강제 실패 발생! 메시지: {}", message);
            throw new RuntimeException("강제 예외 발생");
        }

        log.info("✅ 처리 완료: {}", message);
    }

    @KafkaListener(topics = "retry-topic.DLT", groupId = "retry-dlt-group")
    public void listenDlt(RetryMessage message) {
        log.error("🪦 DLT로 이동된 메시지: {}", message);
    }
}
