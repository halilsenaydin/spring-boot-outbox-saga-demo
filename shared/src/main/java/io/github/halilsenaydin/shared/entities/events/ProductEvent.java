package io.github.halilsenaydin.shared.entities.events;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent {
    private Long id;
    private String name;
    private String description;
    private double unitPrice;
    private List<String> images;
}
