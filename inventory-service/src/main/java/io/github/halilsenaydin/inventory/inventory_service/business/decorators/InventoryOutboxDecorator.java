package io.github.halilsenaydin.inventory.inventory_service.business.decorators;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryOutboxService;
import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.business.constants.InventoryOutboxConstant;
import io.github.halilsenaydin.inventory.inventory_service.business.mapper.InventoryMapper;
import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.Inventory;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.CreateInventoryRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.InventoryResponse;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReleaseRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReserveRequest;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.InventoryEvent;

import org.springframework.transaction.annotation.Transactional;

public class InventoryOutboxDecorator extends InventoryServiceDecorator {
    private final InventoryOutboxService outboxService;

    public InventoryOutboxDecorator(InventoryService delegate,
            InventoryOutboxService outboxService) {
        super(delegate);
        this.outboxService = outboxService;
    }

    @Override
    @Transactional
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> create(CreateInventoryRequest event) {
        OperationResult<Inventory, InventoryResponse, InventoryEvent> result;

        result = delegate.create(event);

        outboxService.save(InventoryOutboxConstant.INVENTORY_CREATED, result.getEvent());

        return result;
    }

    @Override
    @Transactional
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> reserve(StockReserveRequest request) {
        OperationResult<Inventory, InventoryResponse, InventoryEvent> result;

        try {
            result = delegate.reserve(request);

            outboxService.save(InventoryOutboxConstant.INVENTORY_STOCK_RESERVED, InventoryMapper.toStockReservedEvent(result.getEvent(), request));
        } catch (Exception ex) {
            outboxService.saveInNewTransaction(
                InventoryOutboxConstant.INVENTORY_STOCK_RESERVATION_FAILED, 
                InventoryMapper.toStockReservationFailedEvent(request)
            );

            throw ex;
        }

        return result;
    }

    @Override
    @Transactional
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> release(StockReleaseRequest request) {
        OperationResult<Inventory, InventoryResponse, InventoryEvent> result = delegate.release(request);

        outboxService.save(InventoryOutboxConstant.INVENTORY_STOCK_RELEASED, InventoryMapper.toStockReleased(result.getEvent(), request));

        return result;
    }
}
