package io.github.halilsenaydin.inventory.inventory_service.business.messaging;

import io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts.InventoryOutboxRepository;
import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.InventoryOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryOutboxPublisher {
    private final InventoryOutboxRepository outboxRepository;
    private final InventoryOutboxEventPublisher eventPublisher;

    private static final int BATCH_SIZE = 50;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
        PageRequest pageRequest = PageRequest.of(0, BATCH_SIZE);
        Page<InventoryOutbox> page = outboxRepository.findByProcessedFalse(pageRequest);

        while (!page.isEmpty()) {
            page.getContent().forEach(eventPublisher::publishEvent);
            page = outboxRepository.findByProcessedFalse(pageRequest);
        }
    }
}