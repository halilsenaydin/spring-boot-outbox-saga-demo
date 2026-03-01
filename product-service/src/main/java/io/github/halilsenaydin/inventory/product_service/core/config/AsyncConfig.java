package io.github.halilsenaydin.inventory.product_service.core.config;

import io.github.halilsenaydin.shared.core.config.SharedAsyncConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig extends SharedAsyncConfig {
    @Override
    protected String threadNamePrefix() {
        return "ProductService-Publisher-";
    }
}