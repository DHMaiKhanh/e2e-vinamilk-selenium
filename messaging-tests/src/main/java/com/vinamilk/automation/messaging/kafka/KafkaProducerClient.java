package com.vinamilk.automation.messaging.kafka;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaProducerClient implements AutoCloseable {

    private final KafkaProducer<String, String> producer;

    public KafkaProducerClient() {
        this.producer = new KafkaProducer<>(KafkaTestConfig.producerProperties());
    }

    public RecordMetadata publish(String topic, String key, String payload) {
        try {
            return producer.send(new ProducerRecord<>(topic, key, payload)).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new IllegalStateException("Failed to publish message to topic: " + topic, e);
        }
    }

    @Override
    public void close() {
        producer.close();
    }
}
