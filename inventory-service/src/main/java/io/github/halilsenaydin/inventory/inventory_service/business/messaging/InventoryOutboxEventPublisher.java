package io.github.halilsenaydin.inventory.inventory_service.business.messaging;

import io.github.halilsenaydin.inventory.inventory_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.inventory_service.business.constants.InventoryOutboxConstant;
import io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts.InventoryOutboxRepository;
import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.InventoryOutbox;
import io.github.halilsenaydin.shared.business.constants.InventoryEventConstant;
import io.github.halilsenaydin.shared.business.exceptions.OutboxPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryOutboxEventPublisher {

    private final InventoryOutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void publishEvent(InventoryOutbox outbox) {
        try {
            String routingKey = resolveRoutingKey(outbox.getEventType());

            rabbitTemplate.convertAndSend(InventoryEventConstant.INVENTORY_EXCHANGE, routingKey, outbox.getPayload());
            outbox.setProcessed(true);
            outboxRepository.save(outbox);
        } catch (Exception ex) {
            throw new OutboxPublishException("%s: id=%s, eventType=%s"
                    .formatted(ErrorMessage.FAILED_PUBLISH_OUTBOX_EVENT, outbox.getId(), outbox.getEventType()));
        }
    }

    private String resolveRoutingKey(String eventType) {
        return switch (eventType) {
            case InventoryOutboxConstant.INVENTORY_CREATED -> InventoryEventConstant.INVENTORY_RK_CREATED;
            case InventoryOutboxConstant.INVENTORY_CREATION_FAILED ->
                InventoryEventConstant.INVENTORY_RK_CREATION_FAILED;
            case InventoryOutboxConstant.INVENTORY_STOCK_RESERVED -> InventoryEventConstant.INVENTORY_RK_STOCK_RESERVED;
            case InventoryOutboxConstant.INVENTORY_STOCK_RESERVATION_FAILED ->
                InventoryEventConstant.INVENTORY_RK_STOCK_RESERVATION_FAILED;
            case InventoryOutboxConstant.INVENTORY_STOCK_RELEASED -> InventoryEventConstant.INVENTORY_RK_STOCK_RELEASED;
            case InventoryOutboxConstant.INVENTORY_STOCK_RELEASE_FAILED ->
                InventoryEventConstant.INVENTORY_RK_STOCK_RELEASE_FAILED;
            default -> throw new OutboxPublishException(
                    "%s: %s".formatted(eventType, ErrorMessage.UNKNOWN_EVENT_TYPE));
        };
    }
}