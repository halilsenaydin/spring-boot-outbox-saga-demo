package io.github.halilsenaydin.inventory.order_service.business.decorators;

import java.util.List;

import io.github.halilsenaydin.inventory.order_service.business.abstracts.OrderService;
import io.github.halilsenaydin.inventory.order_service.entities.concretes.Order;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CancelOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CreateOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.OrderResponse;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class OrderServiceDecorator implements OrderService {
    protected final OrderService delegate;

    @Override
    public OperationResult<Order, OrderResponse, OrderEvent> create(CreateOrderRequest request) {
        return delegate.create(request);
    }

    @Override
    public OperationResult<Order, OrderResponse, OrderEvent> cancel(CancelOrderRequest request) {
        return delegate.cancel(request);
    }

    @Override
    public List<OrderResponse> getByProductId(Long productId) {
        return delegate.getByProductId(productId);
    }
}
