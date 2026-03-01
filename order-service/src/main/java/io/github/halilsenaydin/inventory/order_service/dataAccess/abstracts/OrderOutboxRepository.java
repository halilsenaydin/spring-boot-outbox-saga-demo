package io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts;

import org.springframework.stereotype.Repository;

import io.github.halilsenaydin.inventory.order_service.entities.concretes.OrderOutbox;
import io.github.halilsenaydin.shared.dataAccess.abstracts.OutboxRepository;

@Repository
public interface OrderOutboxRepository extends OutboxRepository<OrderOutbox> {
}
