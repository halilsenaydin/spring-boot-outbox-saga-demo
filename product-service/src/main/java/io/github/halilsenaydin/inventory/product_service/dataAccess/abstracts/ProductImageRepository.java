package io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductId(Long productId);

    List<ProductImage> findByProductIdAndIsActiveTrue(Long productId);
}