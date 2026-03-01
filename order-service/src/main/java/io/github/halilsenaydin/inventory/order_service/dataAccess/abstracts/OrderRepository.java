package io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.halilsenaydin.inventory.order_service.entities.concretes.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByProductId(Long productId);

    Optional<Order> findByIdAndIsActiveTrue(Long orderId);
}
