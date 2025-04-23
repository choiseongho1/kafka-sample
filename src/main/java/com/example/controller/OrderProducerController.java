package com.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderProducerController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/send")
    public String sendOrderMessages(@RequestParam String key) {
        for (int i = 1; i <= 10; i++) {
            kafkaTemplate.send("order-test-topic", key, "order-" + i);
        }
        return "전송 완료 (key=" + key + ")";
    }
}
