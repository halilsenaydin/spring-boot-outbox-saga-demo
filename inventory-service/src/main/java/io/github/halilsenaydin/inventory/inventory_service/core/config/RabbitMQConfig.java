package io.github.halilsenaydin.inventory.inventory_service.core.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.halilsenaydin.shared.business.constants.InventoryEventConstant;
import io.github.halilsenaydin.shared.core.config.SharedRabbitMQConfig;

@Configuration
public class RabbitMQConfig extends SharedRabbitMQConfig {
    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(InventoryEventConstant.INVENTORY_EXCHANGE, true, false);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_CREATED, true);
    }

    @Bean
    public Queue creationFailedQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_CREATION_FAILED, true);
    }

    @Bean
    public Queue stockReservedQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_STOCK_RESERVED, true);
    }

    @Bean
    public Queue stockReservationFailedQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_STOCK_RESERVATION_FAILED, true);
    }

    @Bean
    public Queue stockReleasedQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_STOCK_RELEASED, true);
    }

    @Bean
    public Queue stockReleaseFailedQueue() {
        return new Queue(InventoryEventConstant.INVENTORY_Q_STOCK_RELEASE_FAILED, true);
    }

    @Bean
    public Binding createdBinding() {
        return BindingBuilder
                .bind(createdQueue())
                .to(inventoryExchange())
                .with(InventoryEventConstant.INVENTORY_RK_CREATED);
    }

    @Bean
    public Binding creationFailedBinding() {
        return BindingBuilder
                .bind(creationFailedQueue())
                .to(inventoryExchange())
                .with(InventoryEventConstant.INVENTORY_RK_CREATION_FAILED);
    }

    @Bean
    public Binding stockReservedBinding() {
        return BindingBuilder
                .bind(stockReservedQueue())
                .to(inventoryExchange())
                .with(InventoryEventConstant.INVENTORY_RK_STOCK_RESERVED);
    }

    @Bean
    public Binding stockReservationFailedBinding() {
        return BindingBuilder
                .bind(stockReservationFailedQueue())
                .to(inventoryExchange())
                .with(InventoryEventConstant.INVENTORY_RK_STOCK_RESERVATION_FAILED);
    }

    @Bean
    public Binding stockReleasedBinding() {
        return BindingBuilder
                .bind(stockReleasedQueue())
                .to(inventoryExchange())
                .with(InventoryEventConstant.INVENTORY_RK_STOCK_RELEASED);
    }

    @Bean
    public Binding stockReleaseFailedBinding() {
        return BindingBuilder
                .bind(stockReleaseFailedQueue())
                .to(inventoryExchange())
                .with(InventoryEventConstant.INVENTORY_RK_STOCK_RELEASE_FAILED);
    }
}