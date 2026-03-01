package io.github.halilsenaydin.inventory.product_service.entities.concretes;

import io.github.halilsenaydin.shared.entities.abstracts.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_images")
@Getter
@Setter
public class ProductImage extends BaseEntity {
    @Column(name = "url", nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductImage() {
    }

    public ProductImage(String url, Product product) {
        this.url = url;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof ProductImage))
            return false;

        return getId() != null && getId() == ((ProductImage) o).getId();
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}