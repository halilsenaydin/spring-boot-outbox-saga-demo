package io.github.halilsenaydin.inventory.product_service.business.exceptions;

import io.github.halilsenaydin.shared.business.exceptions.SharedGlobalExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends SharedGlobalExceptionHandler {
}