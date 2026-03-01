package io.github.halilsenaydin.inventory.order_service.business.concretes;

import io.github.halilsenaydin.inventory.order_service.business.abstracts.OrderService;
import io.github.halilsenaydin.inventory.order_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.order_service.business.mapper.OrderMapper;
import io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts.OrderRepository;
import io.github.halilsenaydin.inventory.order_service.entities.concretes.Order;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CancelOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CreateOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.OrderResponse;
import io.github.halilsenaydin.inventory.order_service.entities.enums.OrderStatus;
import io.github.halilsenaydin.shared.business.exceptions.BusinessException;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderManager implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OperationResult<Order, OrderResponse, OrderEvent> create(CreateOrderRequest request) {
        OperationResult<Order, OrderResponse, OrderEvent> response;

        Order order = new Order(request.getProductId(), request.getQuantity(), OrderStatus.PENDING);

        orderRepository.save(order);

        response = buildResult(order);

        return response;
    }

    @Override
    @Transactional
    public OperationResult<Order, OrderResponse, OrderEvent> cancel(CancelOrderRequest request) {
        Order order = orderRepository.findByIdAndIsActiveTrue(request.getOrderId()).orElseThrow(
            () -> new BusinessException(ErrorMessage.ORDER_NOT_FOUND)
        );

        OperationResult<Order, OrderResponse, OrderEvent> response;

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        response = buildResult(order);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getByProductId(Long productId) {
        return orderRepository.findByProductId(productId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    // Helpers
    private OperationResult<Order, OrderResponse, OrderEvent> buildResult(Order order) {
        return new OperationResult<>(
                order,
                OrderMapper.toResponse(order),
                OrderMapper.toEvent(order));
    }
}
