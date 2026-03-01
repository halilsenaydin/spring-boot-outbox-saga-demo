package io.github.halilsenaydin.inventory.order_service.business.messaging;

import io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts.OrderOutboxRepository;
import io.github.halilsenaydin.inventory.order_service.entities.concretes.OrderOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOutboxPublisher {
    private final OrderOutboxRepository orderOutboxRepository;
    private final OrderOutboxEventPublisher eventPublisher;

    private static final int BATCH_SIZE = 50;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
        PageRequest pageRequest = PageRequest.of(0, BATCH_SIZE);
        Page<OrderOutbox> page = orderOutboxRepository.findByProcessedFalse(pageRequest);

        while (!page.isEmpty()) {
            page.getContent().forEach(eventPublisher::publishEvent);
            page = orderOutboxRepository.findByProcessedFalse(pageRequest);
        }
    }
}