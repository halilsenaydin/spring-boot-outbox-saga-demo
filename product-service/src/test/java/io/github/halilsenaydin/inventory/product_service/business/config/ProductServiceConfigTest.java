package io.github.halilsenaydin.inventory.product_service.business.config;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductOutboxService;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.concretes.ProductOutboxManager;
import io.github.halilsenaydin.inventory.product_service.business.decorators.ProductOutboxDecorator;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceConfigTest {
    @Mock
    private ProductOutboxRepository outboxRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceConfig config;

    @Test
    void inventoryOutboxService_shouldReturnProductOutboxManager() {
        ProductOutboxService service = config.inventoryOutboxService(outboxRepository);

        assertNotNull(service);
        assertInstanceOf(ProductOutboxManager.class, service);
    }

    @Test
    void inventoryService_shouldReturnProductOutboxDecorator() {
        ProductOutboxService outboxService = config.inventoryOutboxService(outboxRepository);
        ProductService service = config.inventoryService(productRepository, outboxService);

        assertNotNull(service);
        assertInstanceOf(ProductOutboxDecorator.class, service);
    }
}