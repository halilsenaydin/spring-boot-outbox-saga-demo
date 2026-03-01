package io.github.halilsenaydin.inventory.inventory_service.business.consumers;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryOutboxService;
import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.business.constants.InventoryOutboxConstant;
import io.github.halilsenaydin.inventory.inventory_service.business.mapper.InventoryMapper;
import io.github.halilsenaydin.shared.business.constants.ProductEventConstant;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.ProductEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventConsumer {

    private final InventoryService inventoryService;
    private final InventoryOutboxService outboxService;

    @RabbitListener(queues = ProductEventConstant.PRODUCT_Q_CREATED, containerFactory = "rabbitListenerContainerFactory")
    public void onProductCreated(String payload) {
        try {
            ProductEvent event = JsonUtil.fromJson(payload, ProductEvent.class);

            inventoryService.create(InventoryMapper.toCreateRequest(event));
        } catch (Exception e) {
            outboxService.saveInNewTransaction(InventoryOutboxConstant.INVENTORY_CREATION_FAILED, payload);
        }
    }
}
