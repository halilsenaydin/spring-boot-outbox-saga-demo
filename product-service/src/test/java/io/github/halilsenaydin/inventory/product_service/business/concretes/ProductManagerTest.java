package io.github.halilsenaydin.inventory.product_service.business.concretes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.halilsenaydin.inventory.product_service.business.constants.ErrorMessage;
import io.github.halilsenaydin.inventory.product_service.dataAccess.abstracts.ProductRepository;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.ProductImage;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.DeleteProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.business.exceptions.BusinessException;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;

@ExtendWith(MockitoExtension.class)
public class ProductManagerTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductManager productManager;

    private CreateProductRequest createRequest;
    private CreateProductRequest createRequestWithImageUrls;
    private Product product;
    private Product productWithImages;
    private ProductImage productImage;
    private String imageUrl;

    @BeforeEach
    void setUp() {
        // Data
        imageUrl = "https://chisellabs.com/blog/what-is-a-product-definition-types-examples/";

        // Requests
        createRequest = new CreateProductRequest("Test Product", "Description", 100.0, null);
        createRequestWithImageUrls = new CreateProductRequest(createRequest.getName(), createRequest.getDescription(),
                createRequest.getUnitPrice(),
                new ArrayList<>(List.of("http://image1.jpg")));

        // Products
        product = new Product(createRequest.getName(), createRequest.getDescription(), createRequest.getUnitPrice(),
                new ArrayList<ProductImage>());
        productWithImages = new Product(createRequestWithImageUrls.getName(),
                createRequestWithImageUrls.getDescription(),
                createRequestWithImageUrls.getUnitPrice(), new ArrayList<ProductImage>());
        productImage = new ProductImage(createRequestWithImageUrls.getImageUrls().get(0), product);

        productImage.setId(1L);
        productWithImages.setProductImages(new ArrayList<ProductImage>(List.of(productImage)));
    }

    @Test
    void create_shouldReturnOperationResult_whenRequestIsValid() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        OperationResult<Product, ProductResponse, ProductEvent> result = productManager.create(createRequest);

        assertNotNull(result);
        assertEquals(createRequest.getName(), result.getData().getName());
        assertEquals(createRequest.getUnitPrice(), result.getData().getUnitPrice());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void create_shouldReturnOperationResult_whenRequestContainImages() {
        when(productRepository.save(any(Product.class))).thenReturn(productWithImages);

        OperationResult<Product, ProductResponse, ProductEvent> result = productManager
                .create(createRequestWithImageUrls);

        assertNotNull(result);
        assertEquals(createRequestWithImageUrls.getName(), result.getData().getName());
        assertEquals(createRequestWithImageUrls.getUnitPrice(), result.getData().getUnitPrice());
        assertEquals(createRequestWithImageUrls.getImageUrls().get(0),
                result.getData().getProductImages().get(0).getUrl());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void delete_shouldDeactivateProduct_whenProductExists() {
        DeleteProductRequest deleteRequest = new DeleteProductRequest(1L);
        
        when(productRepository.findWithImages(1L)).thenReturn(Optional.of(product));

        OperationResult<Product, ProductResponse, ProductEvent> result = productManager.delete(deleteRequest);

        assertFalse(result.getData().isActive());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void delete_shouldThrowBusinessException_whenProductNotFound() {
        DeleteProductRequest deleteRequest = new DeleteProductRequest(99L);

        when(productRepository.findWithImages(99L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> productManager.delete(deleteRequest));

        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getById_shouldReturnProductResponse_whenProductExists() {
        when(productRepository.findWithImages(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productManager.getById(1L);

        assertNotNull(result);
    }

    @Test
    void getById_shouldThrowBusinessException_whenProductNotFound() {
        when(productRepository.findWithImages(99L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> productManager.getById(99L));

        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getByAll_shouldReturnListOfProductResponse_whenProductsListNotEmpty() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponse> result = productManager.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getByAll_shouldReturnListOfEmpty_whenProductsListEmpty() {
        when(productRepository.findAll()).thenReturn(List.of());

        List<ProductResponse> result = productManager.getAll();

        assertEquals(0, result.size());
    }

    @Test
    void addImage_shouldSavedImageOfProduct_whenProductExists() {
        when(productRepository.findWithImages(1L)).thenReturn(Optional.of(productWithImages));

        productManager.addImage(1L, imageUrl);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void addImage_shouldSavedImageOfProduct_whenProductNotFound() {
        when(productRepository.findWithImages(99L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> productManager.addImage(99L, imageUrl));

        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
    }

    @Test
    void removeImage_shouldSavedImageOfProduct_whenProductExists() {
        when(productRepository.findWithImages(1L)).thenReturn(Optional.of(productWithImages));

        productManager.removeImage(1L, 1L);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void removeImage_shouldSavedImageOfProduct_whenProductNotFound() {
        when(productRepository.findWithImages(99L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> productManager.removeImage(99L, 0L));

        assertEquals(ErrorMessage.PRODUCT_NOT_FOUND, exception.getMessage());
    }
}
