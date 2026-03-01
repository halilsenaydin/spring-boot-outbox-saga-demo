package io.github.halilsenaydin.inventory.product_service.api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductsController.class)
class ProductsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse productResponse;
    private CreateProductRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new CreateProductRequest("Product", "Description", 100.0, null);
        productResponse = new ProductResponse(1L, createRequest.getName(), createRequest.getDescription(),
                createRequest.getUnitPrice(), List.of());
    }

    @Test
    void create_shouldReturn200_whenRequestIsValid() throws Exception {
        OperationResult<Product, ProductResponse, ProductEvent> result = new OperationResult<>(null, productResponse,
                null);
        when(productService.create(any())).thenReturn(result);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productResponse.getName()))
                .andExpect(jsonPath("$.unitPrice").value(productResponse.getUnitPrice()));
    }

    @Test
    void getAll_shouldReturn200_whenProductsExist() throws Exception {
        when(productService.getAll()).thenReturn(List.of(productResponse));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(productResponse.getName()));
    }

    @Test
    void getById_shouldReturn200_whenProductExists() throws Exception {
        when(productService.getById(1L)).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()));
    }

    @Test
    void addImage_shouldReturn200_whenRequestIsValid() throws Exception {
        doNothing().when(productService).addImage(1L, "http://image.jpg");

        mockMvc.perform(post("/api/v1/products/1/images")
                .param("url", "http://image.jpg"))
                .andExpect(status().isOk());
    }

    @Test
    void removeImage_shouldReturn204_whenRequestIsValid() throws Exception {
        doNothing().when(productService).removeImage(1L, 1L);

        mockMvc.perform(delete("/api/v1/products/1/images/1"))
                .andExpect(status().isNoContent());
    }
}