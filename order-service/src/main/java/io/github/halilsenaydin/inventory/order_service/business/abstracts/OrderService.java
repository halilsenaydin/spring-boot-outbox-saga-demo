package io.github.halilsenaydin.inventory.order_service.business.abstracts;

import java.util.List;

import io.github.halilsenaydin.inventory.order_service.entities.concretes.Order;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CancelOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CreateOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.OrderResponse;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;

public interface OrderService {
    OperationResult<Order, OrderResponse, OrderEvent> create(CreateOrderRequest request);

    OperationResult<Order, OrderResponse, OrderEvent> cancel(CancelOrderRequest request);

    List<OrderResponse> getByProductId(Long productId);
}
