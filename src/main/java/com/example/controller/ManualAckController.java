package com.example.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ack")
public class ManualAckController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/send")
    public String send(@RequestParam String msg) {
        kafkaTemplate.send("manual-ack-topic", msg);
        return "전송 완료: " + msg;
    }
}
