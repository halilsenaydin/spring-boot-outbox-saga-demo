package io.github.halilsenaydin.inventory.product_service.core.config;

import io.github.halilsenaydin.shared.core.config.SharedSwaggerConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig extends SharedSwaggerConfig {
    @Override
    protected String swaggerTitle() {
        return "Inventory Product Service API";
    }

    @Override
    protected String swaggerDescription() {
        return "Product microservice for inventory domain";
    }

    @Override
    protected String swaggerVersion() {
        return "1.0.0";
    }
}