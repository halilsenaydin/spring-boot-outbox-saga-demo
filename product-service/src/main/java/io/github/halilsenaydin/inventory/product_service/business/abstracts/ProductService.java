package io.github.halilsenaydin.inventory.product_service.business.abstracts;

import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;

import java.util.List;

public interface ProductService {
    OperationResult<Product, ProductResponse, ProductEvent> create(CreateProductRequest request);

    OperationResult<Product, ProductResponse, ProductEvent> delete(DeleteProductRequest request);

    ProductResponse getById(Long id);

    List<ProductResponse> getAll();

    void addImage(Long productId, String imageUrl);

    void removeImage(Long productId, Long imageId);
}