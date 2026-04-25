package com.movie.ticket.paymentservice.exception;

import com.movie.ticket.paymentservice.common.ApiResponse;
import com.movie.ticket.paymentservice.common.ValidationError;
import org.springframework.expression.spel.ast.OpPlus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handlerValidationException(MethodArgumentNotValidException ex){
        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Lỗi dữ liệu đầu vào không hợp lệ")
                .data(null)
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex){
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .data(null)
                .errors(null)
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex){
        ApiResponse<Object> response = ApiResponse.builder()
                .success(false)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Đã có lỗi xảy ra từ phái service" + ex.getMessage())
                .data(null)
                .errors(null)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


}
