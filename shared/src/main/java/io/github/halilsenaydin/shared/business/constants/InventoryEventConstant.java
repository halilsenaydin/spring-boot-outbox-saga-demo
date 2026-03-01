package io.github.halilsenaydin.shared.business.constants;

public class InventoryEventConstant {
    // Exchange
    public static final String INVENTORY_EXCHANGE = "inventory-exchange";

    // Routing Keys
    public static final String INVENTORY_RK_CREATED = "inventory.created";
    public static final String INVENTORY_RK_CREATION_FAILED = "inventory.creation.failed";
    public static final String INVENTORY_RK_STOCK_RESERVED = "inventory.stock.reserved";
    public static final String INVENTORY_RK_STOCK_RESERVATION_FAILED = "inventory.stock.reservation.failed";
    public static final String INVENTORY_RK_STOCK_RELEASED = "inventory.stock.released";
    public static final String INVENTORY_RK_STOCK_RELEASE_FAILED = "inventory.stock.release.failed";

    // Queue Names
    public static final String INVENTORY_Q_CREATED = "inventory-created-queue";
    public static final String INVENTORY_Q_CREATION_FAILED = "inventory-creation-failed-queue";
    public static final String INVENTORY_Q_STOCK_RESERVED = "inventory-stock-reserved-queue";
    public static final String INVENTORY_Q_STOCK_RESERVATION_FAILED = "inventory-stock-reservation-failed-queue";
    public static final String INVENTORY_Q_STOCK_RELEASED = "inventory-stock-released-queue";
    public static final String INVENTORY_Q_STOCK_RELEASE_FAILED = "inventory-stock-release-failed-queue";
}
