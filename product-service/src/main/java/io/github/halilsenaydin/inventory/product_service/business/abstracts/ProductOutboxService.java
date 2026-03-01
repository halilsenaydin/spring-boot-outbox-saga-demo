package io.github.halilsenaydin.inventory.product_service.business.abstracts;

public interface ProductOutboxService {
    <T> void save(String eventType, T event);

    void save(String eventType, String payload);

    <T> void saveInNewTransaction(String eventType, T event);

    void saveInNewTransaction(String eventType, String payload);
}
