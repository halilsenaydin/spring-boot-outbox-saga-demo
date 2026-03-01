package io.github.halilsenaydin.inventory.product_service.business.decorators;

import java.util.List;

import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ProductServiceDecorator implements ProductService {
    protected final ProductService delegate;

    @Override
    public OperationResult<Product, ProductResponse, ProductEvent> create(CreateProductRequest request) {
        return delegate.create(request);
    }

    @Override
    public OperationResult<Product, ProductResponse, ProductEvent> delete(DeleteProductRequest request) {
        return delegate.delete(request);
    }

    @Override
    public ProductResponse getById(Long id) {
        return delegate.getById(id);
    }

    @Override
    public List<ProductResponse> getAll() {
        return delegate.getAll();
    }

    @Override
    public void addImage(Long productId, String imageUrl) {
        delegate.addImage(productId, imageUrl);
    }

    @Override
    public void removeImage(Long productId, Long imageId) {
        delegate.removeImage(productId, imageId);
    }
}
