package com.vinamilk.automation.api.tests.order;

import com.vinamilk.automation.api.base.BaseApiTest;
import com.vinamilk.automation.api.clients.OrderServiceClient;
import com.vinamilk.automation.api.clients.ProductServiceClient;
import com.vinamilk.automation.api.models.request.CreateOrderRequest;
import com.vinamilk.automation.api.models.response.OrderResponse;
import com.vinamilk.automation.api.models.response.ProductResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Exercises order-service against its upstream dependencies (product-service,
 * user-service) rather than mocking them - see mock-services for the isolated
 * variant that stubs those dependencies instead.
 */
@Epic("Microservices API")
@Feature("Order Service")
public class OrderServiceTests extends BaseApiTest {

    private final OrderServiceClient orderServiceClient = new OrderServiceClient();
    private final ProductServiceClient productServiceClient = new ProductServiceClient();

    @Test(description = "Placing an order reserves stock through the product service")
    public void createOrderReducesProductStock() {
        ProductResponse productBefore = productServiceClient.getProduct("SKU-VNM-STV-100");

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("usr-1001");
        request.setShippingAddress("10 Nguyen Van Linh, Q7, HCMC");
        CreateOrderRequest.OrderLineItem item = new CreateOrderRequest.OrderLineItem();
        item.setSku("SKU-VNM-STV-100");
        item.setQuantity(2);
        request.setItems(List.of(item));

        OrderResponse createdOrder = createOrder(request);

        ProductResponse productAfter = productServiceClient.getProduct("SKU-VNM-STV-100");
        Assert.assertEquals(productAfter.getStockQuantity(), productBefore.getStockQuantity() - 2,
                "Product stock should decrease by the ordered quantity across services");
        Assert.assertEquals(createdOrder.getStatus(), "CREATED");
    }

    @Step("Create order via order-service")
    private OrderResponse createOrder(CreateOrderRequest request) {
        Response response = orderServiceClient.createOrder(request);
        response.then().statusCode(201);
        return response.as(OrderResponse.class);
    }
}
