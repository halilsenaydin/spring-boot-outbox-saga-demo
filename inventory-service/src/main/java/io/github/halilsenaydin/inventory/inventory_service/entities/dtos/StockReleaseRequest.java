package io.github.halilsenaydin.inventory.inventory_service.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockReleaseRequest {
    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Positive
    private Long orderId;

    @NotNull
    @Positive
    private Integer quantity;
}