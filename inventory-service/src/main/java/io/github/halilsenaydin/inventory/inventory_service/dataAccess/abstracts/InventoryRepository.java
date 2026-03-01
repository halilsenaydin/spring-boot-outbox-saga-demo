package io.github.halilsenaydin.inventory.inventory_service.dataAccess.abstracts;

import io.github.halilsenaydin.inventory.inventory_service.entities.concretes.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductId(Long productId);

    Optional<Inventory> findByProductIdAndIsActiveTrue(Long productId);
}
