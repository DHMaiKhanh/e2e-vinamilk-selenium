package com.vinamilk.automation.messaging.kafka;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaConsumerClient implements AutoCloseable {

    private final KafkaConsumer<String, String> consumer;

    public KafkaConsumerClient(String topic, String groupIdSuffix) {
        this.consumer = new KafkaConsumer<>(KafkaTestConfig.consumerProperties(groupIdSuffix));
        this.consumer.subscribe(List.of(topic));
    }

    public ConsumerRecord<String, String> awaitMessage(Predicate<String> valuePredicate, Duration timeout) {
        Instant deadline = Instant.now().plus(timeout);
        while (Instant.now().isBefore(deadline)) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                if (valuePredicate.test(record.value())) {
                    return record;
                }
            }
        }
        throw new IllegalStateException("Timed out waiting for a matching message after " + timeout);
    }

    @Override
    public void close() {
        consumer.close();
    }
}
