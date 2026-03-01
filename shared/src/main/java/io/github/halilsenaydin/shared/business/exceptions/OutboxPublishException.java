package io.github.halilsenaydin.shared.business.exceptions;

public class OutboxPublishException extends RuntimeException {
    public OutboxPublishException(String message) {
        super(message);
    }
}