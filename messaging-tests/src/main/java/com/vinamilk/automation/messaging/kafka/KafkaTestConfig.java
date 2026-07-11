package com.vinamilk.automation.messaging.kafka;

import com.vinamilk.automation.core.config.ConfigManager;
import java.util.Properties;

public final class KafkaTestConfig {

    private KafkaTestConfig() {
    }

    public static Properties producerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers());
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        return props;
    }

    public static Properties consumerProperties(String groupIdSuffix) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers());
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", ConfigManager.getInstance().get("kafka.consumer.group") + "-" + groupIdSuffix);
        props.put("auto.offset.reset", "earliest");
        return props;
    }

    public static String bootstrapServers() {
        return ConfigManager.getInstance().get("kafka.bootstrap.servers");
    }

    public static String orderCreatedTopic() {
        return ConfigManager.getInstance().get("kafka.topic.order-created");
    }
}
