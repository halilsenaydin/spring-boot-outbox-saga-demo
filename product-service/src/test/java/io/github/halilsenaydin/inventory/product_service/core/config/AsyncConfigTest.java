package io.github.halilsenaydin.inventory.product_service.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AsyncConfigTest {
    @InjectMocks
    private AsyncConfig asyncConfig;

    @Test
    void threadNamePrefix_shouldReturnCorrectThreadNamePrefix() {
        assertEquals("ProductService-Publisher-", asyncConfig.threadNamePrefix());
    }
}
