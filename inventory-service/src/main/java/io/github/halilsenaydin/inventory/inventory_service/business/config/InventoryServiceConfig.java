package io.github.halilsenaydin.inventory.inventory_service.business.config;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryOutboxService;
import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.business.concretes.InventoryManager;
import io.github.halilsenaydin.inventory.inventory_service.business.concretes.InventoryOutboxManager;
import io.github.halilsenaydin.inventory.inventory_service.business.decorators.InventoryOutboxDecorator;
import io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts.InventoryOutboxRepository;
import io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts.InventoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryServiceConfig {
    @Bean
    public InventoryOutboxService inventoryOutboxService(InventoryOutboxRepository outboxRepository) {
        return new InventoryOutboxManager(outboxRepository);
    }

    @Bean
    public InventoryService inventoryService(InventoryRepository inventoryRepository,
            InventoryOutboxService inventoryOutboxService) {
        InventoryService core = new InventoryManager(inventoryRepository);
        return new InventoryOutboxDecorator(core, inventoryOutboxService);
    }    
}