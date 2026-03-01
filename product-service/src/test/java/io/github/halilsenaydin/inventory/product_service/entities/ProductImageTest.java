package io.github.halilsenaydin.inventory.product_service.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;

@ExtendWith(MockitoExtension.class)
public class ProductImageTest {
    private Product product;
    private Product product2;
    private ProductImage productImage;
    private ProductImage productImage2;
    private ProductImage productImage3;
    private String imageUrl;

    @BeforeEach
    void setUp() {
        // Data
        imageUrl = "https://chisellabs.com/blog/what-is-a-product-definition-types-examples/";
        product = new Product("Product", "Description", 100.0, new ArrayList<ProductImage>());
        product2 = new Product("Product 2", "Description 2", 200.0, new ArrayList<ProductImage>());
        productImage = new ProductImage("", product);
        productImage2 = new ProductImage("", product2);
        productImage3 = new ProductImage();
    }

    @Test
    void constructor_shouldNullProductImageFields() {
        assertEquals(null, productImage3.getUrl());
        assertEquals(null, productImage3.getProduct());
    }

    @Test
    void setUrl_shouldSuccess() {
        productImage.setUrl(imageUrl);

        assertEquals(imageUrl, productImage.getUrl());
    }

    @Test
    void setProduct_shouldSuccess() {
        productImage.setProduct(product2);

        assertEquals(product2, productImage.getProduct());
    }

    @Test
    void equals_shouldReturnTrue_whenSameInstance() {
        assertEquals(productImage, productImage);
    }

    @Test
    void equals_shouldReturnFalse_whenNotProductImage() {
        assertNotEquals(productImage, new Object());
    }

    @Test
    void equals_shouldReturnFalse_whenIdIsNull() {
        assertNotEquals(productImage, productImage2);
    }

    @Test
    void equals_shouldReturnTrue_whenSameId() {
        productImage.setId(1L);
        productImage2.setId(1L);

        assertEquals(productImage, productImage2);
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentId() {
        productImage.setId(1L);
        productImage2.setId(2L);

        assertNotEquals(productImage, productImage2);
    }

    @Test
    void hashCode_shouldReturnSameValue_forAllInstances() {
        assertEquals(productImage.hashCode(), productImage2.hashCode());
    }
}
