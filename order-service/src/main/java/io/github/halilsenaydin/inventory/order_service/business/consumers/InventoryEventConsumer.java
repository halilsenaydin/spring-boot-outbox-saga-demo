package io.github.halilsenaydin.inventory.order_service.business.consumers;

import io.github.halilsenaydin.inventory.order_service.business.abstracts.OrderService;
import io.github.halilsenaydin.inventory.order_service.business.mapper.OrderMapper;
import io.github.halilsenaydin.shared.business.constants.InventoryEventConstant;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.StockReservationFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final OrderService inventoryService;

    @RabbitListener(queues = InventoryEventConstant.INVENTORY_Q_STOCK_RESERVATION_FAILED, containerFactory = "rabbitListenerContainerFactory")
    public void onInventoryReservationFailed(String payload) {
        StockReservationFailedEvent event = JsonUtil.fromJson(payload, StockReservationFailedEvent.class);

        inventoryService.cancel(OrderMapper.toCancelRequest(event));
    }
}
