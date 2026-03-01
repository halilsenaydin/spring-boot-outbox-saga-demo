package io.github.halilsenaydin.inventory.order_service.business.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderOutboxConstant {
    public static final String ORDER_CREATED = "OrderCreated";
    public static final String ORDER_CREATION_FAILED = "OrderCreationFailed";
    public static final String ORDER_CONFIRMED = "OrderConfirmed";
    public static final String ORDER_CANCELLED = "OrderCancelled";
}
