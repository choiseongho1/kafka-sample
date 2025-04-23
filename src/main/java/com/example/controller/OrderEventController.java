package com.example.controller;

import com.example.dto.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderEventController {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @PostMapping("/send")
    public String sendOrder(@RequestBody OrderEvent order) {
        kafkaTemplate.send("order-events", order.getUserId(), order);
        return "✅ 주문 전송 완료";
    }
}
