package io.github.halilsenaydin.inventory.product_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import io.github.halilsenaydin.shared.business.constants.InventoryEventConstant;

import org.springframework.amqp.core.Queue;

@TestConfiguration
public class RabbitTestConfig {
    @Bean
    public Queue inventoryCreationFailedQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_CREATION_FAILED, true);
    }
}