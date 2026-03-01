package io.github.halilsenaydin.inventory.product_service.business.config;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductOutboxService;
import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.concretes.ProductManager;
import io.github.halilsenaydin.inventory.product_service.business.concretes.ProductOutboxManager;
import io.github.halilsenaydin.inventory.product_service.business.decorators.ProductOutboxDecorator;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductOutboxRepository;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductServiceConfig {
    @Bean
    public ProductOutboxService inventoryOutboxService(ProductOutboxRepository outboxRepository) {
        return new ProductOutboxManager(outboxRepository);
    }

    @Bean
    public ProductService inventoryService(ProductRepository inventoryRepository,
            ProductOutboxService inventoryOutboxService) {
        ProductService core = new ProductManager(inventoryRepository);
        return new ProductOutboxDecorator(core, inventoryOutboxService);
    }  
}