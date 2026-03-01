package io.github.halilsenaydin.inventory.product_service.business.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

@ExtendWith(MockitoExtension.class)
public class ProductMapperTest {
    private CreateProductRequest createProductRequest;
    private DeleteProductRequest deleteProductRequest;

    private Product product;
    private ProductResponse productResponse;
    private ProductEvent productEvent;

    @BeforeEach
    void setUp() {
        createProductRequest = new CreateProductRequest("Product", "Description", 100.0, new ArrayList<String>());
        deleteProductRequest = new DeleteProductRequest(1L);

        product = new Product(createProductRequest.getName(), createProductRequest.getDescription(), createProductRequest.getUnitPrice(), new ArrayList<ProductImage>());
        product.setId(1L);
        productResponse = new ProductResponse(1L, product.getName(), product.getDescription(), product.getUnitPrice(),
                new ArrayList<String>());
        productEvent = new ProductEvent(1L, product.getName(), product.getDescription(), product.getUnitPrice(),
                new ArrayList<String>());
    }

    @Test
    void constructor_shouldThrowException_whenInstantiated() throws Exception {
        Constructor<ProductMapper> constructor = ProductMapper.class.getDeclaredConstructor();

        constructor.setAccessible(true);
        
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void toResponse_shouldReturnProductResponse_whenRequestIsProduct() {
        ProductResponse result = ProductMapper.toResponse(product);

        assertEquals(productResponse.getId(), result.getId());
        assertEquals(productResponse.getName(), result.getName());
        assertEquals(productResponse.getDescription(), result.getDescription());
        assertEquals(productResponse.getUnitPrice(), result.getUnitPrice());
        assertEquals(productResponse.getImages(), result.getImages());
    }

    @Test
    void toEvent_shouldReturnProductResponse_whenRequestIsProduct() {
        ProductEvent result = ProductMapper.toEvent(product);

        assertEquals(productResponse.getId(), result.getId());
        assertEquals(productResponse.getName(), result.getName());
        assertEquals(productResponse.getDescription(), result.getDescription());
        assertEquals(productResponse.getUnitPrice(), result.getUnitPrice());
        assertEquals(productResponse.getImages(), result.getImages());
    }

    @Test
    void toCreateRequest_shouldReturnCreateProductRequest_whenRequestIsProductEvent() {
        CreateProductRequest result = ProductMapper.toCreateRequest(productEvent);

        assertEquals(productResponse.getName(), result.getName());
        assertEquals(productResponse.getDescription(), result.getDescription());
        assertEquals(productResponse.getUnitPrice(), result.getUnitPrice());
        assertEquals(productResponse.getImages(), result.getImageUrls());
    }

    @Test
    void toDeleteRequest_shouldReturnDeleteProductRequest_whenRequestIsProductEvent() {
        DeleteProductRequest result = ProductMapper.toDeleteRequest(productEvent);

        assertEquals(deleteProductRequest.getProductId(), result.getProductId());
    }
}
