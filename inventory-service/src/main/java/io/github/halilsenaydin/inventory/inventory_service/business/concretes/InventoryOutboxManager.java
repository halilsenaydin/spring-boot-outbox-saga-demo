package io.github.halilsenaydin.inventory.inventory_service.business.concretes;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryOutboxService;
import io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts.InventoryOutboxRepository;
import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.InventoryOutbox;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryOutboxManager implements InventoryOutboxService {
    private final InventoryOutboxRepository inventoryOutboxRepository;

    @Override
    @Transactional
    public <T> void save(String eventType, T event) {
        saveOutbox(eventType, event);
    }

    @Override
    @Transactional
    public void save(String eventType, String payload) {
        InventoryOutbox outbox = new InventoryOutbox();

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
        InventoryOutbox outbox = new InventoryOutbox();

        outbox.setEventType(eventType);
        outbox.setPayload(payload);
        
        inventoryOutboxRepository.save(outbox);
    }

    private <T> void saveOutbox(String eventType, T event) {
        InventoryOutbox outbox = new InventoryOutbox();

        outbox.setEventType(eventType);
        outbox.setPayload(JsonUtil.convertToJson(event));
        
        inventoryOutboxRepository.save(outbox);
    }
}
