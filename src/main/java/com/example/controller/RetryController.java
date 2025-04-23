package com.example.controller;

import com.example.dto.RetryMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retry")
public class RetryController {

    private final KafkaTemplate<String, RetryMessage> kafkaTemplate;

    @PostMapping("/send")
    public String send(@RequestBody RetryMessage message) {
        kafkaTemplate.send("retry-topic", message);
        return "전송 완료";
    }
}