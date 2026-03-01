package io.github.halilsenaydin.inventory.inventory_service.business.mapper;

import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.Inventory;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.CreateInventoryRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.InventoryResponse;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReleaseRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReserveRequest;
import io.github.halilsenaydin.shared.entities.events.InventoryEvent;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import io.github.halilsenaydin.shared.entities.events.StockReleasedEvent;
import io.github.halilsenaydin.shared.entities.events.StockReservationFailedEvent;
import io.github.halilsenaydin.shared.entities.events.StockReservedEvent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class InventoryMapper {
    public static InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getReservedQuantity(),
                inventory.getAvailableQuantity());
    }

    public static InventoryEvent toEvent(Inventory inventory) {
        return new InventoryEvent(
                inventory.getId(),
                inventory.getProductId(),
                inventory.getQuantity(),
                inventory.getAvailableQuantity());
    }

    public static CreateInventoryRequest toCreateRequest(ProductEvent event) {
        return new CreateInventoryRequest(
                event.getId(),
                null
            );
    }

    public static StockReserveRequest toReserveRequest(OrderEvent event) {
        return new StockReserveRequest(
                event.getProductId(),
                event.getId(),
                event.getQuantity()
            );
    }

    public static StockReleaseRequest toReleaseRequest(OrderEvent event) {
        return new StockReleaseRequest(
                event.getProductId(),
                event.getId(),
                event.getQuantity()
            );
    }

    public static StockReservedEvent toStockReservedEvent(InventoryEvent event, StockReserveRequest request) {
        return StockReservedEvent.builder()
                .id(event.getId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .availableQuantity(event.getAvailableQuantity())
                .orderId(request.getOrderId())
                .build();
    }

    public static StockReleasedEvent toStockReleased(InventoryEvent event, StockReleaseRequest request) {
        return StockReleasedEvent.builder()
                .id(event.getId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .availableQuantity(event.getAvailableQuantity())
                .orderId(request.getOrderId())
                .build();
    }

    public static StockReservationFailedEvent toStockReservationFailedEvent(StockReserveRequest request) {
        return StockReservationFailedEvent.builder()
                .id(null)
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .availableQuantity(0)
                .orderId(request.getOrderId())
                .build();
    }
}
