package com.minifood.foodservice.common.exception;

import com.minifood.foodservice.common.api.ApiError;
import com.minifood.foodservice.common.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ApiError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toApiError)
                .toList();

        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(400, "Validation Error", errors));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<ApiError> errors = ex.getConstraintViolations().stream()
                .map(violation -> new ApiError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(400, "Du lieu khong hop le", errors));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.fail(404, ex.getMessage(), null));
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessLogicException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponse.fail(422, ex.getMessage(), null));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.fail(401, ex.getMessage(), null));
    }

    @ExceptionHandler({ServiceUnavailableException.class, DataAccessResourceFailureException.class, CannotCreateTransactionException.class})
    public ResponseEntity<ApiResponse<Void>> handleServiceUnavailable(Exception ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.fail(503, ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnknown(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(500, "He thong dang gap loi. Vui long thu lai sau", null));
    }

    private ApiError toApiError(FieldError fieldError) {
        return new ApiError(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
