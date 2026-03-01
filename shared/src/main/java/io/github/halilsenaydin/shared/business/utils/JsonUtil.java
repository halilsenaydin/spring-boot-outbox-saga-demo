package io.github.halilsenaydin.shared.business.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.halilsenaydin.shared.business.constants.ErrorMessage;
import io.github.halilsenaydin.shared.business.exceptions.BusinessException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new BusinessException(String.format("%s: %s", ErrorMessage.JSON_SERIALIZATION_ERROR, e.getMessage()));
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException(String.format("%s: %s", ErrorMessage.JSON_DESERIALIZATION_ERROR, e.getMessage()));
        }
    }
}