package com.example.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.Windowed;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
public class KafkaWindowConsumer {

   @KafkaListener(
       topics = "user-order-window",
       groupId = "window-monitor",
       containerFactory = "windowedListenerFactory"
   )
   public void listen(
       @Header(KafkaHeaders.RECEIVED_KEY) String rawKey,
       Integer count
   ) {
       // Windowed<String> 형식의 키 파싱
       String[] parts = rawKey.split("@");
       String userId = parts[0];
       String[] windowTimes = parts[1].split("/");

       long windowStart = Long.parseLong(windowTimes[0]);
       long windowEnd = Long.parseLong(windowTimes[1]);

       String start = Instant.ofEpochMilli(windowStart).toString();
       String end = Instant.ofEpochMilli(windowEnd).toString();

       log.info("⏱️ 윈도우 수신 → userId={}, window={}~{}, count={}", userId, start, end, count);
   }
}