package io.github.halilsenaydin.inventory.product_service.business.decorators;

import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductOutboxService;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.constants.ProductOutboxConstant;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

import org.springframework.transaction.annotation.Transactional;

public class ProductOutboxDecorator extends ProductServiceDecorator {
    private final ProductOutboxService outboxService;

    public ProductOutboxDecorator(ProductService delegate,
            ProductOutboxService outboxService) {
        super(delegate);
        this.outboxService = outboxService;
    }

    @Override
    @Transactional
    public OperationResult<Product, ProductResponse, ProductEvent> create(CreateProductRequest request) {
        OperationResult<Product, ProductResponse, ProductEvent> response = delegate.create(request);

        outboxService.save(ProductOutboxConstant.PRODUCT_CREATED_EVENT, response.getEvent());

        return response;
    }
}