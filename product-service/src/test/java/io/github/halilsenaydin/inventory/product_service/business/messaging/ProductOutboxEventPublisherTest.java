package io.github.halilsenaydin.inventory.product_service.business.messaging;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import io.github.halilsenaydin.inventory.product_service.business.constants.ProductOutboxConstant;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.shared.business.constants.ProductEventConstant;
import io.github.halilsenaydin.shared.business.exceptions.OutboxPublishException;

@ExtendWith(MockitoExtension.class)
public class ProductOutboxEventPublisherTest {
    @Mock
    private ProductOutboxRepository outboxRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ProductOutboxEventPublisher outboxEventPublisher;

    static Stream<Arguments> provideEventTypes() {
        return Stream.of(
                Arguments.of(ProductOutboxConstant.PRODUCT_CREATED_EVENT, ProductEventConstant.PRODUCT_RK_CREATED),
                Arguments.of(ProductOutboxConstant.PRODUCT_CREATION_FAILED_EVENT,
                        ProductEventConstant.PRODUCT_RK_CREATION_FAILED));
    }

    @ParameterizedTest
    @MethodSource("provideEventTypes")
    void publishEvent_shouldSendWithCorrectRoutingKey_whenEventTypeIsValid(
            String eventType, String expectedRoutingKey) {

        ProductOutbox outbox = new ProductOutbox();
        outbox.setEventType(eventType);
        outbox.setPayload("{}");

        outboxEventPublisher.publishEvent(outbox);

        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(ProductEventConstant.PRODUCT_EXCHANGE),
                eq(expectedRoutingKey),
                any(String.class));

        clearInvocations(rabbitTemplate);
    }

    @Test
    void publishEvent_shouldThrowException_whenEventTypeIsUnknown() {
        ProductOutbox outbox = new ProductOutbox();
        outbox.setEventType("UnknownEvent");
        outbox.setPayload("{}");

        assertThrows(OutboxPublishException.class,
                () -> outboxEventPublisher.publishEvent(outbox));
    }
}
