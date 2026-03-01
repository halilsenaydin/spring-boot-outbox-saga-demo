package io.github.halilsenaydin.inventory.product_service.business.consumers;

import io.github.halilsenaydin.inventory.product_service.BaseIntegrationTest;
import io.github.halilsenaydin.inventory.product_service.entities.concretes.Product;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.CreateProductRequest;
import io.github.halilsenaydin.inventory.product_service.entities.dtos.ProductResponse;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryEventConsumerIT extends BaseIntegrationTest {
    @Autowired
    private InventoryEventConsumer inventoryEventConsumer;

    @Test
    void onInventoryCreationFailed_shouldDeactivateOldProduct_andCreateNewProduct() {
        CreateProductRequest request = new CreateProductRequest("Product", "Description", 100.0, null);
        ResponseEntity<ProductResponse> created = restTemplate.postForEntity(
                "/api/v1/products",
                request,
                ProductResponse.class);

        Long id = created.getBody().getId();

        ProductEvent event = new ProductEvent();
        event.setId(id);
        event.setName(created.getBody().getName());
        event.setDescription(created.getBody().getDescription());
        event.setUnitPrice(created.getBody().getUnitPrice());

        String payload = JsonUtil.convertToJson(event);

        inventoryEventConsumer.onInventoryCreationFailed(payload);

        Product oldProduct = productRepository.findById(id).get();

        assertFalse(oldProduct.isActive());

        List<Product> activeProducts = productRepository.findAll()
                .stream()
                .filter(Product::isActive)
                .toList();

        assertEquals(1, activeProducts.size());
        assertEquals(request.getName(), activeProducts.get(0).getName());
    }
}