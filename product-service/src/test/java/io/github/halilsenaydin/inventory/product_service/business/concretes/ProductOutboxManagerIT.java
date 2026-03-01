package io.github.halilsenaydin.inventory.product_service.business.concretes;

import io.github.halilsenaydin.inventory.product_service.BaseIntegrationTest;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductOutboxService;
import io.github.halilsenaydin.inventory.product_service.business.constants.ProductOutboxConstant;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductOutboxManagerIT extends BaseIntegrationTest {
    @Autowired
    private ProductOutboxService outboxManager;

    @Test
    void save_shouldPersistOutboxRecord_whenEventObjectProvided() {
        ProductEvent event = new ProductEvent();

        event.setId(1L);
        event.setName("Product");

        outboxManager.save(ProductOutboxConstant.PRODUCT_CREATED_EVENT, event);

        List<ProductOutbox> records = outboxRepository.findAll();

        assertEquals(1, records.size());
        assertEquals(ProductOutboxConstant.PRODUCT_CREATED_EVENT, records.get(0).getEventType());
        assertNotNull(records.get(0).getPayload());
        assertFalse(records.get(0).isProcessed());
    }

    @Test
    void save_shouldPersistOutboxRecord_whenStringPayloadProvided() {
        outboxManager.save(ProductOutboxConstant.PRODUCT_CREATED_EVENT, "{}");

        List<ProductOutbox> records = outboxRepository.findAll();

        assertEquals(1, records.size());
        assertEquals(ProductOutboxConstant.PRODUCT_CREATED_EVENT, records.get(0).getEventType());
        assertEquals("{}", records.get(0).getPayload());
        assertFalse(records.get(0).isProcessed());
    }

    @Test
    void saveInNewTransaction_shouldPersistOutboxRecord_whenEventObjectProvided() {
        ProductEvent event = new ProductEvent();

        event.setId(1L);
        event.setName("Product");

        outboxManager.saveInNewTransaction(ProductOutboxConstant.PRODUCT_CREATION_FAILED_EVENT, event);

        List<ProductOutbox> records = outboxRepository.findAll();

        assertEquals(1, records.size());
        assertEquals(ProductOutboxConstant.PRODUCT_CREATION_FAILED_EVENT, records.get(0).getEventType());
        assertNotNull(records.get(0).getPayload());
    }

    @Test
    void saveInNewTransaction_shouldPersistOutboxRecord_whenStringPayloadProvided() {
        outboxManager.saveInNewTransaction(ProductOutboxConstant.PRODUCT_CREATION_FAILED_EVENT, "{}");

        List<ProductOutbox> records = outboxRepository.findAll();

        assertEquals(1, records.size());
        assertEquals(ProductOutboxConstant.PRODUCT_CREATION_FAILED_EVENT, records.get(0).getEventType());
        assertEquals("{}", records.get(0).getPayload());
    }
}