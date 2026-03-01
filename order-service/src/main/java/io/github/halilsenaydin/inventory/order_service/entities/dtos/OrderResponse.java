package io.github.halilsenaydin.inventory.order_service.entities.dtos;

import io.github.halilsenaydin.inventory.order_service.entities.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long productId;
    private int quantity;
    private OrderStatus status;
}