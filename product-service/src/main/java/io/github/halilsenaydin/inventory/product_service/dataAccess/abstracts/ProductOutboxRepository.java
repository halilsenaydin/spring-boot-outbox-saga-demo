package io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductOutbox;
import io.github.halilsenaydin.shared.dataAccess.abstracts.OutboxRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductOutboxRepository extends OutboxRepository<ProductOutbox> {
}