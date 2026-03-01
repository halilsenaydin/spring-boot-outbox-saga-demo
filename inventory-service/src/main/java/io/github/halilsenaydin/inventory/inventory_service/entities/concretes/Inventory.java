package io.github.halilsenaydin.inventory.inventory_service.entities.concretes;

import io.github.halilsenaydin.shared.entities.abstracts.BaseEntity;
import io.github.halilsenaydin.inventory.inventory_service.business.exceptions.InsufficientStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventories")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Inventory extends BaseEntity {
    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity = 0;

    public int getAvailableQuantity() {
        return quantity - reservedQuantity;
    }

    public void reserve(int amount) {
        if (getAvailableQuantity() < amount) {
            throw new InsufficientStockException(productId, amount, getAvailableQuantity());
        }

        this.reservedQuantity += amount;
    }

    public void release(int amount) {
        this.reservedQuantity = Math.max(0, this.reservedQuantity - amount);
    }
}