package io.github.halilsenaydin.inventory.product_service.core.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JacksonConfigTest {
    @InjectMocks
    private JacksonConfig jacksonConfig;

    @Test
    void constructor_shouldCreateInstance() {
        assertNotNull(jacksonConfig);
    }
}