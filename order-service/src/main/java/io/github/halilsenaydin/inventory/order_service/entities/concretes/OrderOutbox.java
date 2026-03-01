package io.github.halilsenaydin.inventory.order_service.entities.concretes;

import io.github.halilsenaydin.shared.entities.abstracts.BaseOutbox;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_outbox")
@Getter
@Setter
@NoArgsConstructor
public class OrderOutbox extends BaseOutbox {
}