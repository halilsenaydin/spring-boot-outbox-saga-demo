package io.github.halilsenaydin.inventory.inventory_service.business.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InventoryOutboxConstant {
    public static final String INVENTORY_CREATED = "InventoryCreated";
    public static final String INVENTORY_CREATION_FAILED = "InventoryCreationFailed";
    public static final String INVENTORY_STOCK_RESERVED = "InventoryStockReserved";
    public static final String INVENTORY_STOCK_RESERVATION_FAILED = "InventoryStockReservationFailed";
    public static final String INVENTORY_STOCK_RELEASED = "InventoryStockReleased";
    public static final String INVENTORY_STOCK_RELEASE_FAILED = "InventoryStockReleaseFailed";
}
