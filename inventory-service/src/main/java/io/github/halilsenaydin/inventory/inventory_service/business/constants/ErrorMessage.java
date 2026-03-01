package io.github.halilsenaydin.inventory.inventory_service.business.constants;

public class ErrorMessage {
    public static final String UNKNOWN_EVENT_TYPE = "Unknown inventory event type";
    public static final String FAILED_PUBLISH_OUTBOX_EVENT = "Failed to publish outbox event";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock";
    public static final String REQUESTED = "Requested";
    public static final String AVAILABLE = "Available";
    public static final String INVENTORY_NOT_FOUND = "Inventory not found";
}
