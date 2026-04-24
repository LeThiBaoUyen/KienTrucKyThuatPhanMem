package com.minifood.foodservice.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        Instant timestamp,
        boolean success,
        int code,
        String message,
        T data,
        List<ApiError> errors
) {

    public static <T> ApiResponse<T> ok(int code, String message, T data) {
        return new ApiResponse<>(Instant.now(), true, code, message, data, null);
    }

    public static <T> ApiResponse<T> fail(int code, String message, List<ApiError> errors) {
        return new ApiResponse<>(Instant.now(), false, code, message, null, errors);
    }
}
