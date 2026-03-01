package io.github.halilsenaydin.inventory.order_service.entities.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelOrderRequest {
    @NotNull
    @Positive
    private Long productId;

    @NotNull
    @Positive
    private Long orderId;
}
