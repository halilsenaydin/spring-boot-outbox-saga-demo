package io.github.halilsenaydin.inventory.product_service.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SwaggerConfigTest {
    @InjectMocks
    private SwaggerConfig swaggerConfig;

    @Test
    void swaggerTitle_shouldReturnCorrectTitle() {
        assertEquals("Inventory Product Service API", swaggerConfig.swaggerTitle());
    }

    @Test
    void swaggerDescription_shouldReturnCorrectDescription() {
        assertEquals("Product microservice for inventory domain", swaggerConfig.swaggerDescription());
    }

    @Test
    void swaggerVersion_shouldReturnCorrectVersion() {
        assertEquals("1.0.0", swaggerConfig.swaggerVersion());
    }
}
