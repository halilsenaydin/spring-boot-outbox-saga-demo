package io.github.halilsenaydin.inventory.product_service.entities.dtos;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private double unitPrice;

    private List<String> imageUrls;
}