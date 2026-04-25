package com.movie.ticket.paymentservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedEvent {
    @NotNull(message = "bookingId không được để trống")
    @Positive(message = "bookingId phải lớn hơn 0")
    private Long bookingId;

    @NotNull(message = "userId không được để trống")
    @Positive(message = "userId phải lớn hơn 0")
    private Long userId;

    @NotNull(message = "amount không được để trống")
    @Positive(message = "amount phải lớn hơn 0")
    private Double amount;
}
