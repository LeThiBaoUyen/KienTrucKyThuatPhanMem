package com.movie.ticket.paymentservice.consumer;

import com.movie.ticket.paymentservice.config.RabbitMQConfig;
import com.movie.ticket.paymentservice.dtos.BookingCreatedEvent;
import com.movie.ticket.paymentservice.dtos.PaymentResultEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_CREATED_QUEUE)
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info(">> [PAYMENT] Nhận yêu cầu thanh toán cho Đơn hàng #{}, Số tiền: {}",
                event.getBookingId(), event.getAmount());

        // Giả lập xử lý thanh toán mất 2 giây
        try { Thread.sleep(2000); } catch (InterruptedException e) {}

        // Logic xử lý ngẫu nhiên: 80% thành công, 20% thất bại
        boolean isSuccess = Math.random() > 0.2;

        PaymentResultEvent result = PaymentResultEvent.builder()
                .bookingId(event.getBookingId())
                .userId(event.getUserId())
                .status(isSuccess ? "SUCCESS" : "FAILED")
                .message(isSuccess ? "Thanh toán hoàn tất" : "Số dư không đủ")
                .build();

        // Bắn event báo kết quả
        String routingKey = isSuccess ? RabbitMQConfig.PAYMENT_COMPLETED_KEY : RabbitMQConfig.BOOKING_FAILED_KEY;

        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EXCHANGE, routingKey, result);

        log.info("<< [PAYMENT] Đã xử lý xong Đơn hàng #{} - Kết quả: {}",
                event.getBookingId(), result.getStatus());
    }
}
