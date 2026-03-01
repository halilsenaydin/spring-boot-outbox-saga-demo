package io.github.halilsenaydin.inventory.order_service.business.messaging;

import io.github.halilsenaydin.inventory.order_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.order_service.business.constants.OrderOutboxConstant;
import io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts.OrderOutboxRepository;
import io.github.halilsenaydin.inventory.order_service.entities.concretes.OrderOutbox;
import io.github.halilsenaydin.shared.business.constants.OrderEventConstant;
import io.github.halilsenaydin.shared.business.exceptions.OutboxPublishException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOutboxEventPublisher {
    private final OrderOutboxRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void publishEvent(OrderOutbox outbox) {
        try {
            String routingKey = resolveRoutingKey(outbox.getEventType());

            rabbitTemplate.convertAndSend(OrderEventConstant.ORDER_EXCHANGE, routingKey, outbox.getPayload());
            outbox.setProcessed(true);
            productRepository.save(outbox);
        } catch (Exception ex) {
            throw new OutboxPublishException("%s: id=%s, eventType=%s"
                    .formatted(ErrorMessage.FAILED_PUBLISH_OUTBOX_EVENT, outbox.getId(), outbox.getEventType()));
        }
    }

    private String resolveRoutingKey(String eventType) {
        return switch (eventType) {
            case OrderOutboxConstant.ORDER_CREATED -> OrderEventConstant.ORDER_RK_CREATED;
            case OrderOutboxConstant.ORDER_CREATION_FAILED -> OrderEventConstant.ORDER_RK_CREATION_FAILED;
            default -> throw new OutboxPublishException(
                    "%s: %s".formatted(eventType, ErrorMessage.UNKNOWN_EVENT_TYPE));
        };
    }
}