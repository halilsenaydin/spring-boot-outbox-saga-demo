package io.github.halilsenaydin.inventory.order_service.api.controllers;

import io.github.halilsenaydin.inventory.order_service.business.abstracts.OrderService;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.CreateOrderRequest;
import io.github.halilsenaydin.inventory.order_service.entities.dtos.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order query APIs")
public class OrdersController {
    private final OrderService orderService;

    @Operation(summary = "Create order")
    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.create(request).getResponse();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get orders by product id")
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderResponse>> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(orderService.getByProductId(productId));
    }
}
