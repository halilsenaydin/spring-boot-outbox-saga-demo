package io.github.halilsenaydin.inventory.inventory_service.entities.concretes;

import io.github.halilsenaydin.shared.entities.abstracts.BaseOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory_outbox")
@Getter
@Setter
@NoArgsConstructor
public class InventoryOutbox extends BaseOutbox {
}