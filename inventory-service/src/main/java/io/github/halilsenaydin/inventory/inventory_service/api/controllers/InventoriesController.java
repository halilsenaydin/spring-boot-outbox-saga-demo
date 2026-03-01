package io.github.halilsenaydin.inventory.inventory_service.api.controllers;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.CreateInventoryRequest;
import io.github.halilsenaydin.inventory.inventory_service.entities.dtos.InventoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/inventories")
@RequiredArgsConstructor
@Tag(name = "Inventories", description = "Inventory query APIs")
public class InventoriesController {

    private final InventoryService inventoryService;

    @Operation(summary = "Create inventory if not exist inventory else update quantity")
    @PostMapping
    public ResponseEntity<InventoryResponse> create(
            @Valid @RequestBody CreateInventoryRequest request) {
        InventoryResponse response = inventoryService.create(request).getResponse();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get inventory by product id")
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getByProductId(productId));
    }

    @Operation(summary = "Check if product has enough available stock")
    @GetMapping("/product/{productId}/available/{quantity}")
    public ResponseEntity<Boolean> isAvailable(
            @PathVariable Long productId,
            @PathVariable int quantity) {
        InventoryResponse inventory = inventoryService.getByProductId(productId);
        
        return ResponseEntity.ok(inventory.getAvailableQuantity() >= quantity);
    }
}
