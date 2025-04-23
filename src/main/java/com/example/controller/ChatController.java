package com.example.controller;

import com.example.dto.ChatMessage;
import com.example.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public String send(@RequestBody ChatMessage msg) {
        kafkaProducerService.sendJsonMessage("chat-topic", msg);
        return "전송 완료: " + msg;
    }

    @PostMapping("/partition/send")
    public String sendWithKey(@RequestParam String key, @RequestParam String msg) {
        kafkaProducerService.sendWithKey("partition-test-topic", key, msg);
        return "전송 완료";
    }
}