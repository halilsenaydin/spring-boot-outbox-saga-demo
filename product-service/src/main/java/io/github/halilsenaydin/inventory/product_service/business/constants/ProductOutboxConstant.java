package io.github.halilsenaydin.inventory.product_service.business.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductOutboxConstant {
    public static final String PRODUCT_CREATED_EVENT = "ProductCreatedEvent";
    public static final String PRODUCT_CREATION_FAILED_EVENT = "ProductCreationFailedEvent";
}
