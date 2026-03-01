package io.github.halilsenaydin.inventory.inventory_service.business.decorators;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.Inventory;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.CreateInventoryRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.InventoryResponse;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReleaseRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReserveRequest;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.InventoryEvent;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class InventoryServiceDecorator implements InventoryService {
    protected final InventoryService delegate;

    @Override
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> create(CreateInventoryRequest request) {
        return delegate.create(request);
    }

    @Override
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> reserve(StockReserveRequest request) {
        return delegate.reserve(request);
    }

    @Override
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> release(StockReleaseRequest request) {
        return delegate.release(request);
    }

    @Override
    public InventoryResponse getByProductId(Long productId) {
        return delegate.getByProductId(productId);
    }
}
