package io.github.halilsenaydin.shared.entities.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long id;
    private Long productId;
    private int quantity;
    private String status;
}
