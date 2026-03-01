package io.github.halilsenaydin.inventory.product_service.business.mapper;

import java.util.List;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

public class ProductMapper {
    private ProductMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ProductResponse toResponse(Product product) {
        List<String> images = product.getProductImages()
                .stream()
                .map(ProductImage::getUrl)
                .toList();

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                images);
    }

    public static ProductEvent toEvent(Product product) {
        List<String> images = product.getProductImages()
                .stream()
                .map(ProductImage::getUrl)
                .toList();

        return new ProductEvent(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getUnitPrice(),
                images);
    }

    public static CreateProductRequest toCreateRequest(ProductEvent event) {
        return new CreateProductRequest(
                event.getName(),
                event.getDescription(),
                event.getUnitPrice(),
                event.getImages());
    }

    public static DeleteProductRequest toDeleteRequest(ProductEvent event) {
        return new DeleteProductRequest(event.getId());
    }
}