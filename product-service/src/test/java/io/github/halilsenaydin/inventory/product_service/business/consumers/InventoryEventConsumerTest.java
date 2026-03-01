package io.github.halilsenaydin.inventory.product_service.business.consumers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.mapper.ProductMapper;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

@ExtendWith(MockitoExtension.class)
public class InventoryEventConsumerTest {
    @Mock
    private ProductService productService;

    @InjectMocks
    private InventoryEventConsumer inventoryEventConsumer;

    private ProductEvent productEvent;
    
    @BeforeEach
    void setUp() {
        Product product = new Product("Product", "Description", 100.0, new ArrayList<>());
    
        productEvent = ProductMapper.toEvent(product);
    }

    @Test
    void onInventoryCreationFailed_shouldRetryCreateProduct() {
        String payload = JsonUtil.convertToJson(productEvent);

        inventoryEventConsumer.onInventoryCreationFailed(payload);

        verify(productService, times(1)).delete(any());
        verify(productService, times(1)).create(any());
    }
}
