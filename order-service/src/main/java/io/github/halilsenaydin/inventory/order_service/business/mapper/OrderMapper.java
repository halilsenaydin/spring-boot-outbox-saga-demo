package io.github.halilsenaydin.inventory.order_service.business.mapper;

import io.github.halilsenaydin.inventory.order_service.entities.concretes.Order;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CancelOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.OrderResponse;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;
import io.github.halilsenaydin.shared.entities.events.StockReservationFailedEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class OrderMapper {
    public static OrderResponse toResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getStatus()
        );
    }

    public static OrderEvent toEvent(Order order) {
        return new OrderEvent(
                order.getId(),
                order.getProductId(),
                order.getQuantity(),
                order.getStatus().toString());
    }

    public static CancelOrderRequest toCancelRequest(StockReservationFailedEvent event) {
        return new CancelOrderRequest(
                event.getProductId(),
                event.getOrderId());
    }
}
