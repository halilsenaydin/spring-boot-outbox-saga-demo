package io.github.halilsenaydin.inventory.inventory_service.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateInventoryRequest {
    @NotNull
    @Positive
    private Long productId;

    @Positive
    private Integer quantity;
}