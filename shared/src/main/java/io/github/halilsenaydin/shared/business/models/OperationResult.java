package io.github.halilsenaydin.shared.business.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationResult<D, R, E> {
    private D data;
    private R response;
    private E event;
}