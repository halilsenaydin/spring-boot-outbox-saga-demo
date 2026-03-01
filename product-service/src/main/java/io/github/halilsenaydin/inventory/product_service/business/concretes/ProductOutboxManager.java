package io.github.halilsenaydin.inventory.product_service.business.concretes;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductOutboxService;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductOutboxManager implements ProductOutboxService {
    private final ProductOutboxRepository inventoryOutboxRepository;

    @Override
    @Transactional
    public <T> void save(String eventType, T event) {
        saveOutbox(eventType, event);
    }

    @Override
    @Transactional
    public void save(String eventType, String payload) {
        ProductOutbox outbox = new ProductOutbox();

        outbox.setEventType(eventType);
        outbox.setPayload(payload);
        
        inventoryOutboxRepository.save(outbox);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T> void saveInNewTransaction(String eventType, T event) {
        saveOutbox(eventType, event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInNewTransaction(String eventType, String payload) {
        ProductOutbox outbox = new ProductOutbox();

        outbox.setEventType(eventType);
        outbox.setPayload(payload);
        
        inventoryOutboxRepository.save(outbox);
    }

    private <T> void saveOutbox(String eventType, T event) {
        ProductOutbox outbox = new ProductOutbox();

        outbox.setEventType(eventType);
        outbox.setPayload(JsonUtil.convertToJson(event));
        
        inventoryOutboxRepository.save(outbox);
    }
}
