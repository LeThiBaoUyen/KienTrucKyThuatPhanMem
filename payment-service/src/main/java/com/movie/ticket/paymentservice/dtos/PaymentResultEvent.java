package com.movie.ticket.paymentservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResultEvent {
    private Long bookingId;
    private Long userId;
    private String status;
    private String message;
}
