package io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    List<Product> findByUnitPriceBetween(double min, double max);

    List<Product> findByIsActiveTrue();

    Optional<Product> findByIdAndIsActiveTrue(Long id);

    @Query("""
    SELECT p FROM Product p
    LEFT JOIN FETCH p.productImages
    WHERE p.id = :id AND p.isActive = true
    """)
    Optional<Product> findWithImages(@Param("id") Long id);
}