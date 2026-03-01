package io.github.halilsenaydin.inventory.product_service.core.config;

import io.github.halilsenaydin.shared.business.constants.ProductEventConstant;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQConfigTest {
    @InjectMocks
    private RabbitMQConfig rabbitMQConfig;

    @Test
    void productExchange_shouldReturnCorrectExchange() {
        TopicExchange exchange = rabbitMQConfig.productExchange();

        assertNotNull(exchange);
        assertEquals(ProductEventConstant.PRODUCT_EXCHANGE, exchange.getName());
        assertTrue(exchange.isDurable());
    }

    @Test
    void createdQueue_shouldReturnCorrectQueue() {
        Queue queue = rabbitMQConfig.createdQueue();

        assertNotNull(queue);
        assertEquals(ProductEventConstant.PRODUCT_Q_CREATED, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void creationFailedQueue_shouldReturnCorrectQueue() {
        Queue queue = rabbitMQConfig.creationFailedQueue();

        assertNotNull(queue);
        assertEquals(ProductEventConstant.PRODUCT_Q_CREATION_FAILED, queue.getName());
        assertTrue(queue.isDurable());
    }

    @Test
    void createdBinding_shouldBindCorrectQueueToExchange() {
        Binding binding = rabbitMQConfig.createdBinding();

        assertNotNull(binding);
        assertEquals(ProductEventConstant.PRODUCT_Q_CREATED, binding.getDestination());
        assertEquals(ProductEventConstant.PRODUCT_RK_CREATED, binding.getRoutingKey());
        assertEquals(ProductEventConstant.PRODUCT_EXCHANGE, binding.getExchange());
    }

    @Test
    void creationFailedBinding_shouldBindCorrectQueueToExchange() {
        Binding binding = rabbitMQConfig.creationFailedBinding();

        assertNotNull(binding);
        assertEquals(ProductEventConstant.PRODUCT_Q_CREATION_FAILED, binding.getDestination());
        assertEquals(ProductEventConstant.PRODUCT_RK_CREATION_FAILED, binding.getRoutingKey());
        assertEquals(ProductEventConstant.PRODUCT_EXCHANGE, binding.getExchange());
    }
}