package io.github.halilsenaydin.inventory.inventory_service.business.abstracts;

import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.Inventory;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.CreateInventoryRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.InventoryResponse;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReleaseRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReserveRequest;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.InventoryEvent;

public interface InventoryService {
    OperationResult<Inventory, InventoryResponse, InventoryEvent> create(CreateInventoryRequest request);

    OperationResult<Inventory, InventoryResponse, InventoryEvent> reserve(StockReserveRequest event);

    OperationResult<Inventory, InventoryResponse, InventoryEvent> release(StockReleaseRequest event);

    InventoryResponse getByProductId(Long productId);
}
