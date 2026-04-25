package com.movie.ticket.paymentservice.common;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean success;
    private int code;
    private String message;
    private T data;
    private Object errors;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .code(200)
                .message(message)
                .data(data)
                .errors(null)
                .build();
    }
}
