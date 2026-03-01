package io.github.halilsenaydin.inventory.inventory_service.entities.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    private Long id;
    private Long productId;
    private int quantity;
    private int reservedQuantity;
    private int availableQuantity;
}