package com.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concurrency")
public class ConcurrencyTestController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/bulk")
    public String sendBulk() {
        for (int i = 1; i <= 30; i++) {
            kafkaTemplate.send("concurrency-test-topic", "msg-" + i);
        }
        return "30개 메시지 전송 완료!";
    }
}