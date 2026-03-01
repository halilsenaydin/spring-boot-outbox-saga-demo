package io.github.halilsenaydin.inventory.product_service.business.decorators;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductOutboxService;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.constants.ProductOutboxConstant;
import io.github.halilsenaydin.inventory.product_service.business.mapper.ProductMapper;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

@ExtendWith(MockitoExtension.class)
public class ProductOutboxDecoratorTest {
    @Mock
    private ProductOutboxService productOutboxService;

    @Mock
    private ProductService delegate;

    @InjectMocks
    private ProductOutboxDecorator productOutboxDecorator;

    CreateProductRequest createProductRequest;
    DeleteProductRequest deleteProductRequest;
    Product product;
    ProductResponse productResponse;
    ProductEvent productEvent;
    ProductOutbox createProductOutbox;
    OperationResult<Product, ProductResponse, ProductEvent> productOperationResult;

    @BeforeEach
    void setUp() {
        // Requests
        createProductRequest = new CreateProductRequest("Test Product", "Description", 100.0, null);
        deleteProductRequest = new DeleteProductRequest(1L);

        // Products
        product = new Product(createProductRequest.getName(), createProductRequest.getDescription(),
                createProductRequest.getUnitPrice(),
                new ArrayList<ProductImage>());
        productEvent = ProductMapper.toEvent(product);
        productResponse = ProductMapper.toResponse(product);
        createProductOutbox = new ProductOutbox();
        createProductOutbox.setEventType(ProductOutboxConstant.PRODUCT_CREATED_EVENT);
        productOperationResult = new OperationResult<Product, ProductResponse, ProductEvent>(product, productResponse,
                productEvent);
    }

    @Test
    void create_shouldTriggerCreatedProductEvent_whenRequestIsValid() {
        when(delegate.create(any(CreateProductRequest.class))).thenReturn(productOperationResult);

        productOutboxDecorator.create(createProductRequest);

        verify(productOutboxService, times(1)).save(
                ProductOutboxConstant.PRODUCT_CREATED_EVENT,
                productEvent);
    }

    @Test
    void create_shouldDelegateToDelegate_whenNotOverridden() {
        ProductServiceDecorator plainDecorator = new ProductServiceDecorator(delegate) {};

        plainDecorator.create(createProductRequest);

        verify(delegate, times(1)).create(createProductRequest);
    }

    @Test
    void delete_shouldNotTriggerEvent_whenRequestIsValid() {
        productOutboxDecorator.delete(deleteProductRequest);

        verify(productOutboxService, never()).save(any(), any());
    }

    @Test
    void getById_shouldNotTriggerEvent_whenRequestIsValid() {
        productOutboxDecorator.getById(1L);

        verify(productOutboxService, never()).save(any(), any());
    }

    @Test
    void getAll_shouldNotTriggerEvent_whenRequestIsValid() {
        productOutboxDecorator.getAll();

        verify(productOutboxService, never()).save(any(), any());
    }

    @Test
    void addImage_shouldNotTriggerEvent_whenRequestIsValid() {
        productOutboxDecorator.addImage(1L, "");

        verify(productOutboxService, never()).save(any(), any());
    }

    @Test
    void removeImage_shouldNotTriggerEvent_whenRequestIsValid() {
        productOutboxDecorator.removeImage(1L, 99L);

        verify(productOutboxService, never()).save(any(), any());
    }
}
