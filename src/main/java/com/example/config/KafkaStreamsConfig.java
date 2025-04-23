package com.example.config;

import com.example.dto.OrderEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration streamsConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-aggregate-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass()); // JSON이면 커스텀 필요
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, Integer> kStream(StreamsBuilder builder) {
        KStream<String, Integer> stream = builder
            .stream("order-events", Consumed.with(Serdes.String(), new JsonSerde<>(OrderEvent.class)))
            .groupByKey()
            .aggregate(
                () -> 0,
                (key, event, agg) -> agg + event.getQuantity(),
                Materialized.with(Serdes.String(), Serdes.Integer())
            )
            .toStream();

        stream.peek((key, count) ->
            System.out.println("🧾 userId=" + key + ", total=" + count)
        ).to("user-order-count", Produced.with(Serdes.String(), Serdes.Integer()));

        return stream;
    }

    @Bean
    public KStream<Windowed<String>, Integer> kStreamWindow(StreamsBuilder builder) {
        Duration windowSize = Duration.ofMinutes(5); // ⏱️ 5분 윈도우

        KTable<Windowed<String>, Integer> windowTable = builder
            .stream("order-events", Consumed.with(Serdes.String(), new JsonSerde<>(OrderEvent.class)))
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeWithNoGrace(windowSize))
            .aggregate(
                () -> 0,
                (key, event, agg) -> agg + event.getQuantity(),
                Materialized.with(Serdes.String(), Serdes.Integer())
            );

        KStream<Windowed<String>, Integer> stream = windowTable.toStream();

        // 결과 전송
        stream.peek((key, value) -> {
            String start = Instant.ofEpochMilli(key.window().start()).toString();
            String end = Instant.ofEpochMilli(key.window().end()).toString();
            System.out.println("🪟 집계 window=" + start + " ~ " + end + ", user=" + key.key() + ", total=" + value);
        }).to("user-order-window", Produced.with(WindowedSerdes.timeWindowedSerdeFrom(String.class), Serdes.Integer()));

        return stream;
    }


}
