package io.github.halilsenaydin.inventory.product_service.business.messaging;

import io.github.halilsenaydin.inventory.product_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.product_service.business.constants.ProductOutboxConstant;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.shared.business.constants.ProductEventConstant;
import io.github.halilsenaydin.shared.business.exceptions.OutboxPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductOutboxEventPublisher {
    private final ProductOutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void publishEvent(ProductOutbox outbox) {
        try {
            String routingKey = resolveRoutingKey(outbox.getEventType());

            rabbitTemplate.convertAndSend(ProductEventConstant.PRODUCT_EXCHANGE, routingKey, outbox.getPayload());
            outbox.setProcessed(true);
            outboxRepository.save(outbox);
        } catch (Exception ex) {
            throw new OutboxPublishException("%s: id=%s, eventType=%s"
                    .formatted(ErrorMessage.FAILED_PUBLISH_OUTBOX_EVENT, outbox.getId(), outbox.getEventType()));
        }
    }

    private String resolveRoutingKey(String eventType) {
        return switch (eventType) {
            case ProductOutboxConstant.PRODUCT_CREATED_EVENT -> ProductEventConstant.PRODUCT_RK_CREATED;
            case ProductOutboxConstant.PRODUCT_CREATION_FAILED_EVENT -> ProductEventConstant.PRODUCT_RK_CREATION_FAILED;
            default -> throw new OutboxPublishException(
                    "%s: %s".formatted(eventType, ErrorMessage.UNKNOWN_EVENT_TYPE));
        };
    }
}