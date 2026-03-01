package io.github.halilsenaydin.inventory.product_service.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;

@ExtendWith(MockitoExtension.class)
public class ProductTest {
    private Product product;
    private ProductImage productImage;
    private String imageUrl;

    @BeforeEach
    void setUp() {
        // Data
        imageUrl = "https://chisellabs.com/blog/what-is-a-product-definition-types-examples/";
        product = new Product("Product", "Description", 100.0, new ArrayList<ProductImage>());
        productImage = new ProductImage(imageUrl, product);
    }

    @Test
    void setName_shouldSuccess() {
        product.setName("Product 25");

        assertEquals("Product 25", product.getName());
    }

    @Test
    void setDescription_shouldSuccess() {
        product.setDescription("Description 25");

        assertEquals("Description 25", product.getDescription());
    }

    @Test
    void setUnitPrice_shouldSuccess() {
        product.setUnitPrice(100.0);

        assertEquals(100.0, product.getUnitPrice());
    }

    @Test
    void setProductImages_shouldSuccess() {
        product.setProductImages(new ArrayList<ProductImage>(List.of(productImage)));

        assertEquals(1, product.getProductImages().size());
        assertEquals(imageUrl, product.getProductImages().get(0).getUrl());
    }

    @Test
    void addImage_shouldSuccess() {
        product.addImage(productImage);

        assertEquals(1, product.getProductImages().size());
        assertEquals(imageUrl, product.getProductImages().get(0).getUrl());
    }

    @Test
    void removeImage_shouldSuccess() {
        product.setProductImages(new ArrayList<ProductImage>(List.of(productImage)));

        assertEquals(1, product.getProductImages().size());
        assertEquals(imageUrl, product.getProductImages().get(0).getUrl());

        product.removeImage(productImage);

        assertEquals(0, product.getProductImages().size());
    }
}
