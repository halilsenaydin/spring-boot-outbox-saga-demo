package io.github.halilsenaydin.inventory.inventory_service.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockReserveRequest {
    @NotNull
    private Long productId;

    @NotNull
    private Long orderId;

    @NotNull
    @Positive
    private Integer quantity;
}