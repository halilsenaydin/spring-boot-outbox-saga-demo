package io.github.halilsenaydin.inventory.product_service.business.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorMessage {
    public static final String UNKNOWN_EVENT_TYPE = "Unknown product event type";
    public static final String FAILED_PUBLISH_OUTBOX_EVENT = "Failed to publish outbox event";
    public static final String PRODUCT_NOT_FOUND = "Product not found";
}
