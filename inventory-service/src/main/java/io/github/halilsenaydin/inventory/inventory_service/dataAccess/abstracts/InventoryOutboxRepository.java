package io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts;

import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.InventoryOutbox;
import io.github.halilsenaydin.shared.dataAccess.abstracts.OutboxRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface InventoryOutboxRepository extends OutboxRepository<InventoryOutbox> {
}
