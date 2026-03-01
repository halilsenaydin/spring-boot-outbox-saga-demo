package io.github.halilsenaydin.inventory.product_service.business.consumers;

import io.github.halilsenaydin.inventory.product_service.business.abstracts.ProductService;
import io.github.halilsenaydin.inventory.product_service.business.mapper.ProductMapper;
import io.github.halilsenaydin.shared.business.constants.InventoryEventConstant;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final ProductService productService;

    @RabbitListener(queues = InventoryEventConstant.INVENTORY_Q_CREATION_FAILED, containerFactory = "rabbitListenerContainerFactory")
    public void onInventoryCreationFailed(String payload) {
        ProductEvent event = JsonUtil.fromJson(payload, ProductEvent.class);

        // Soft delete
        productService.delete(ProductMapper.toDeleteRequest(event));

        // Retry creation product
        productService.create(ProductMapper.toCreateRequest(event));
    }
}
