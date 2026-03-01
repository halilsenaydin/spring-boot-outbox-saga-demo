package io.github.halilsenaydin.inventory.inventory_service.business.concretes;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.inventory_service.business.mapper.InventoryMapper;
import io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts.InventoryRepository;
import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.Inventory;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.CreateInventoryRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.InventoryResponse;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReleaseRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.StockReserveRequest;
import io.github.halilsenaydin.shared.business.exceptions.BusinessException;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.InventoryEvent;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class InventoryManager implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> create(CreateInventoryRequest request) {
        Long productId = request.getProductId();
        Optional<Inventory> inventory = inventoryRepository.findByProductId(productId);
        boolean alreadyExists = inventory.isPresent();
        Integer quantity = request.getQuantity();

        if (alreadyExists) {
            Inventory existInventory = inventory.get();

            existInventory.setQuantity(quantity);

            return this.buildResult(existInventory);
        }

        Inventory newInventory = new Inventory(
                productId,
                quantity != null ? quantity : 0,
                0);

        inventoryRepository.save(newInventory);

        return this.buildResult(newInventory);
    }

    @Override
    @Transactional
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> reserve(StockReserveRequest request) {
        Inventory inventory = findActiveByProductId(request.getProductId());

        inventory.reserve(request.getQuantity());

        return buildResult(inventory);
    }

    @Override
    @Transactional
    public OperationResult<Inventory, InventoryResponse, InventoryEvent> release(StockReleaseRequest request) {
        Inventory inventory = findActiveByProductId(request.getProductId());

        inventory.release(request.getQuantity());

        return buildResult(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getByProductId(Long productId) {
        return InventoryMapper.toResponse(findActiveByProductId(productId));
    }

    // Helpers
    private Inventory findActiveByProductId(Long productId) {
        return inventoryRepository.findByProductIdAndIsActiveTrue(productId).orElseThrow(
                () -> new BusinessException(ErrorMessage.INVENTORY_NOT_FOUND));
    }

    private OperationResult<Inventory, InventoryResponse, InventoryEvent> buildResult(Inventory inventory) {
        return new OperationResult<>(
                inventory,
                InventoryMapper.toResponse(inventory),
                InventoryMapper.toEvent(inventory));
    }
}
