package io.github.halilsenaydin.inventory.product_service.api.controllers;

import io.github.halilsenaydin.inventory.product_service.BaseIntegrationTest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductsControllerIT extends BaseIntegrationTest {
    @Test
    void create_shouldPersistProduct_andCreateOutboxEvent() {
        CreateProductRequest request = new CreateProductRequest(
                "Product", "Description", 100.0, null);

        ResponseEntity<ProductResponse> response = restTemplate.postForEntity(
                "/api/v1/products", request, ProductResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(request.getName(), response.getBody().getName());

        assertEquals(1, outboxRepository.findAll().size());
        assertFalse(outboxRepository.findAll().get(0).isProcessed());
    }

    @Test
    void getAll_shouldReturnAllProducts() {
        restTemplate.postForEntity("/api/v1/products",
                new CreateProductRequest("Product 1", "Description", 100.0, null),
                ProductResponse.class);
        restTemplate.postForEntity("/api/v1/products",
                new CreateProductRequest("Product 2", "Description", 200.0, null),
                ProductResponse.class);

        ResponseEntity<List<ProductResponse>> response = restTemplate.exchange(
                "/api/v1/products",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductResponse>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getById_shouldReturnProduct_whenProductExists() {
        CreateProductRequest request = new CreateProductRequest("Product", "Description", 100.0, null);
        ResponseEntity<ProductResponse> created = restTemplate.postForEntity(
                "/api/v1/products",
                request,
                ProductResponse.class);

        Long id = created.getBody().getId();

        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
                "/api/v1/products/" + id, ProductResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(request.getName(), response.getBody().getName());
    }

    @Test
    void getById_shouldThrowException_whenProductNotFound() {
        ResponseEntity<ProductResponse> response = restTemplate.getForEntity(
                "/api/v1/products/" + 99L, ProductResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void addImage_shouldReturn200_whenProductExists() {
        ResponseEntity<ProductResponse> created = restTemplate.postForEntity(
                "/api/v1/products",
                new CreateProductRequest("Product", "Description", 100.0, null),
                ProductResponse.class);

        Long id = created.getBody().getId();

        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/v1/products/" + id + "/images?url=http://image.jpg",
                null,
                Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResponseEntity<ProductResponse> product = restTemplate.getForEntity(
                "/api/v1/products/" + id, ProductResponse.class);

        assertEquals(1, product.getBody().getImages().size());
        assertEquals("http://image.jpg", product.getBody().getImages().get(0));
    }

    @Test
    void addImage_shouldReturn400_whenProductNotFound() {
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/v1/products/" + 99L + "/images?url=http://image.jpg",
                null,
                Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void removeImage_shouldReturn204_whenImageExists() {
        ResponseEntity<ProductResponse> created = restTemplate.postForEntity(
                "/api/v1/products",
                new CreateProductRequest("Product", "Description", 100.0,
                        List.of("http://image.jpg")),
                ProductResponse.class);

        Long productId = created.getBody().getId();
        Long imageId = productRepository.findWithImages(productId)
                .get().getProductImages().get(0).getId();

        restTemplate.delete("/api/v1/products/" + productId + "/images/" + imageId);

        ResponseEntity<ProductResponse> product = restTemplate.getForEntity(
                "/api/v1/products/" + productId, ProductResponse.class);

        assertEquals(0, product.getBody().getImages().size());
    }

    @Test
    void removeImage_shouldReturn400_whenProductNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/v1/products/99/images/99",
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}