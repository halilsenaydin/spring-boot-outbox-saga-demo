package io.github.halilsenaydin.inventory.product_service.entities.concretes;

import io.github.halilsenaydin.shared.entities.abstracts.BaseOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_outbox")
@Getter
@Setter
@NoArgsConstructor
public class ProductOutbox extends BaseOutbox {
}