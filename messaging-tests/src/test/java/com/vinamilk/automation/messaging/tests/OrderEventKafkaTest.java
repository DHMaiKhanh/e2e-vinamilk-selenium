package com.vinamilk.automation.messaging.tests;

import com.vinamilk.automation.api.clients.OrderServiceClient;
import com.vinamilk.automation.api.models.request.CreateOrderRequest;
import com.vinamilk.automation.messaging.kafka.KafkaConsumerClient;
import com.vinamilk.automation.messaging.kafka.KafkaTestConfig;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.time.Duration;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Event-Driven Microservices")
@Feature("Order Events")
public class OrderEventKafkaTest {

    @Test(description = "order-service publishes an order-created event consumable by downstream services")
    public void orderCreationPublishesKafkaEvent() {
        String customerId = "usr-1001";

        try (KafkaConsumerClient consumerClient =
                     new KafkaConsumerClient(KafkaTestConfig.orderCreatedTopic(), "order-event-test")) {

            CreateOrderRequest request = new CreateOrderRequest();
            request.setCustomerId(customerId);
            request.setShippingAddress("10 Nguyen Van Linh, Q7, HCMC");
            CreateOrderRequest.OrderLineItem item = new CreateOrderRequest.OrderLineItem();
            item.setSku("SKU-VNM-STV-100");
            item.setQuantity(1);
            request.setItems(List.of(item));

            new OrderServiceClient().createOrder(request).then().statusCode(201);

            ConsumerRecord<String, String> event = consumerClient.awaitMessage(
                    payload -> payload.contains(customerId), Duration.ofSeconds(30));

            Assert.assertTrue(event.value().contains("\"status\":\"CREATED\""),
                    "Expected the order-created event to report a CREATED status");
        }
    }
}
