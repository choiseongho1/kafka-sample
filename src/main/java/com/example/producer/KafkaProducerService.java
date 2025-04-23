package com.example.producer;

import com.example.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, ChatMessage> chatMessageKafkaTemplate;
    private final KafkaTemplate<String, String> stringKafkaTemplate;

    public void sendJsonMessage(String topic, ChatMessage message) {
        chatMessageKafkaTemplate.send(topic, message);
    }

    public void sendWithKey(String topic, String key, String value) {
        stringKafkaTemplate.send(topic, key, value);
    }
}