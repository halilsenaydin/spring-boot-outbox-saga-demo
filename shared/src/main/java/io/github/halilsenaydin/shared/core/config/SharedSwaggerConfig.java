package io.github.halilsenaydin.shared.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SharedSwaggerConfig {
    protected String swaggerTitle() {
        return "Service API";
    }

    protected String swaggerDescription() {
        return "Service API documentation";
    }

    protected String swaggerVersion() {
        return "1.0.0";
    }

    @Bean
    public OpenAPI inventoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(swaggerTitle())
                        .description(swaggerDescription())
                        .version(swaggerVersion()));
    }
}