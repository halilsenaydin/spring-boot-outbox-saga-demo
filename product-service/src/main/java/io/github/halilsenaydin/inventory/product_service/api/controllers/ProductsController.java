package io.github.halilsenaydin.inventory.product_service.api.controllers;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductsController {

    private final ProductService productService;

    @Operation(summary = "Create product")
    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody CreateProductRequest request) {
        ProductResponse response = productService.create(request).getResponse();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "List all products")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }

    @Operation(summary = "Get product detail using id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        ProductResponse response = productService.getById(id);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add product image for product")
    @PostMapping("/{id}/images")
    public ResponseEntity<Void> addImage(
            @PathVariable Long id,
            @RequestParam String url) {
        productService.addImage(id, url);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete product image from product")
    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Void> removeImage(
            @PathVariable Long productId,
            @PathVariable Long imageId) {
        productService.removeImage(productId, imageId);

        return ResponseEntity.noContent().build();
    }
}