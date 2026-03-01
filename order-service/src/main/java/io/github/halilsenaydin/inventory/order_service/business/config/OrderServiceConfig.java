package io.github.halilsenaydin.inventory.order_service.business.config;

import io.github.halilsenaydin.inventory.order_service.business.abstracts.OrderService;
import io.github.halilsenaydin.inventory.order_service.business.concretes.OrderManager;
import io.github.halilsenaydin.inventory.order_service.business.decorators.OrderOutboxDecorator;
import io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts.OrderOutboxRepository;
import io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts.OrderRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceConfig {
    @Bean
    public OrderService inventoryService(OrderRepository inventoryRepository,
            OrderOutboxRepository outboxRepository) {
        OrderService core = new OrderManager(inventoryRepository);

        return new OrderOutboxDecorator(core, outboxRepository);
    }
}