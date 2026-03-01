package io.github.halilsenaydin.inventory.inventory_service.business.consumers;

import io.github.halilsenaydin.inventory.inventory_service.business.abstracts.InventoryService;
import io.github.halilsenaydin.inventory.inventory_service.business.mapper.InventoryMapper;
import io.github.halilsenaydin.shared.business.constants.OrderEventConstant;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final InventoryService inventoryService;

    @RabbitListener(queues = OrderEventConstant.ORDER_Q_CREATED, containerFactory = "rabbitListenerContainerFactory")
    public void onOrderCreated(String payload) {
        OrderEvent event = JsonUtil.fromJson(payload, OrderEvent.class);

        try {
            inventoryService.reserve(InventoryMapper.toReserveRequest(event));
        } catch (Exception ex) {
            System.out.println("Reservation failed for order " + event.getId() + ": " + ex.getMessage());
        }
    }
}
