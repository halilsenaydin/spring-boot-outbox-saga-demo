package io.github.halilsenaydin.inventory.product_service.business.concretes;

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

import io.github.halilsenaydin.inventory.product_service.business.mapper.ProductMapper;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

@ExtendWith(MockitoExtension.class)
public class ProductOutboxManagerTest {
    @Mock
    private ProductOutboxRepository outboxRepository;

    @InjectMocks
    private ProductOutboxManager productOutboxManager;

    private String eventType;
    private Product product;
    private ProductEvent productEvent;
    private String productPayload;

    @BeforeEach
    void setupUp() {
        eventType = "EventType";
        product = new Product("Product", "Description", 100.0, new ArrayList<>());
        productEvent = ProductMapper.toEvent(product);
        productPayload = "{name: 'Product', description: 'Description', unitPrice: 100.0, productImages: []}";
    }

    @Test
    void save_shouldCreatedOutbox_whenRequestIsEventType() {
        productOutboxManager.save(eventType, productEvent);

        verify(outboxRepository, times(1)).save(any(ProductOutbox.class));
    }

    @Test
    void save_shouldCreatedOutbox_whenRequestIsStringType() {
        productOutboxManager.save(eventType, productPayload);

        verify(outboxRepository, times(1)).save(any(ProductOutbox.class));
    }

    @Test
    void saveInNewTransaction_shouldCreatedOutbox_whenRequestIsEventType() {
        productOutboxManager.saveInNewTransaction(eventType, productEvent);

        verify(outboxRepository, times(1)).save(any(ProductOutbox.class));
    }

    @Test
    void saveInNewTransaction_shouldCreatedOutbox_whenRequestIsStringType() {
        productOutboxManager.saveInNewTransaction(eventType, productPayload);

        verify(outboxRepository, times(1)).save(any(ProductOutbox.class));
    }
}
