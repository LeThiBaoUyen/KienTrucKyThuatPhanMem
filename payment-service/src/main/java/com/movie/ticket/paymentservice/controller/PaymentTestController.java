package com.movie.ticket.paymentservice.controller;

import com.movie.ticket.paymentservice.common.ApiResponse;
import com.movie.ticket.paymentservice.config.RabbitMQConfig;
import com.movie.ticket.paymentservice.dtos.BookingCreatedEvent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentTestController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/test")
    public ResponseEntity<ApiResponse<String>> triggerPayment(@Valid @RequestBody BookingCreatedEvent event) {
        rabbitTemplate.convertAndSend("", RabbitMQConfig.BOOKING_CREATED_QUEUE, event);

        ApiResponse<String> response = ApiResponse.success(
                "bookingId=" + event.getBookingId(),
                "Đã gửi yêu cầu thanh toán vào queue"
        );

        return ResponseEntity.accepted().body(response);
    }
}
