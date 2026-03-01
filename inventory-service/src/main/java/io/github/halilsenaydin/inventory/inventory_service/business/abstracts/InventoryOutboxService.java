package io.github.halilsenaydin.inventory.inventory_service.business.abstracts;

public interface InventoryOutboxService {
    <T> void save(String eventType, T event);

    void save(String eventType, String payload);

    <T> void saveInNewTransaction(String eventType, T event);

    void saveInNewTransaction(String eventType, String payload);
}
