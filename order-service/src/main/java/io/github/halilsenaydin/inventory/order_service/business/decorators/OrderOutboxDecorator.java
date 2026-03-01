package io.github.halilsenaydin.inventory.order_service.business.decorators;

import io.github.halilsenaydin.inventory.order_service.business.abstracts.OrderService;
import io.github.halilsenaydin.inventory.order_service.business.constants.OrderOutboxConstant;
import io.github.halilsenaydin.inventory.order_service.dataAccess.abstracts.OrderOutboxRepository;
import io.github.halilsenaydin.inventory.order_service.entities.concretes.Order;
import io.github.halilsenaydin.inventory.order_service.entities.concretes.OrderOutbox;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CreateOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.OrderResponse;
import io.github.halilsenaydin.shared.business.models.OperationResult;
import io.github.halilsenaydin.shared.business.utils.JsonUtil;
import io.github.halilsenaydin.shared.entities.events.OrderEvent;

import org.springframework.transaction.annotation.Transactional;

public class OrderOutboxDecorator extends OrderServiceDecorator {
    private final OrderOutboxRepository outboxRepository;

    public OrderOutboxDecorator(OrderService delegate,
            OrderOutboxRepository outboxRepository) {
        super(delegate);
        this.outboxRepository = outboxRepository;
    }

    @Override
    @Transactional
    public OperationResult<Order, OrderResponse, OrderEvent> create(CreateOrderRequest event) {
        OperationResult<Order, OrderResponse, OrderEvent> result;

        result = delegate.create(event);

        saveOutbox(OrderOutboxConstant.ORDER_CREATED, result.getEvent());

        return result;
    }

    // Helpers
    private void saveOutbox(String eventType, OrderEvent event) {
        OrderOutbox outbox = new OrderOutbox();

        outbox.setEventType(eventType);
        outbox.setPayload(JsonUtil.convertToJson(event));
        outboxRepository.save(outbox);
    }
}
