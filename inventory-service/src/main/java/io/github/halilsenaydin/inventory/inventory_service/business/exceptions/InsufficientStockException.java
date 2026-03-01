package io.github.halilsenaydin.inventory.inventory_service.business.exceptions;

import io.github.halilsenaydin.inventory.inventory_service.business.constants.ErrorMessage;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(Long productId, int requested, int available) {
        super(String.format(
                "%s=%d. %s: %d, %s: %d",
                ErrorMessage.INSUFFICIENT_STOCK, productId, ErrorMessage.REQUESTED, requested, ErrorMessage.AVAILABLE, available));
    }
}
