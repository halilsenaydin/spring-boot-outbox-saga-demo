package io.github.halilsenaydin.inventory.product_service.business.messaging;

import io.github.halilsenaydin.inventory.product_service.BaseIntegrationTest;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductOutboxPublisherIT extends BaseIntegrationTest {
    @Test
    void publishEvents_shouldMarkOutboxAsProcessed_afterSuccessfulPublish()
            throws InterruptedException {

        restTemplate.postForEntity("/api/v1/products",
                new CreateProductRequest("Product", "Description", 100.0, null),
                ProductResponse.class);

        List<ProductOutbox> unprocessed = outboxRepository.findByProcessedFalse();

        assertEquals(1, unprocessed.size());

        Thread.sleep(7000);

        List<ProductOutbox> processed = outboxRepository.findAll();

        assertTrue(processed.stream().allMatch(ProductOutbox::isProcessed));
    }
}