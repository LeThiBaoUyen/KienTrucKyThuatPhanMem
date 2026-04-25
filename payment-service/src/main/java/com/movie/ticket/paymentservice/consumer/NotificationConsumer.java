package com.movie.ticket.paymentservice.consumer;

import com.movie.ticket.paymentservice.config.RabbitMQConfig;
import com.movie.ticket.paymentservice.dtos.PaymentResultEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumer {

    // Nghe sự kiện THANH TOÁN THÀNH CÔNG
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "notification.success.queue"),
            exchange = @Exchange(name = RabbitMQConfig.PAYMENT_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConfig.PAYMENT_COMPLETED_KEY
    ))
    public void handlePaymentSuccess(PaymentResultEvent event) {
        log.info("🔔 [NOTIFICATION] THÔNG BÁO: Booking #{} thành công!", event.getBookingId());
        log.info("🔔 [NOTIFICATION] Log: User {} đã đặt đơn #{} thành công.", event.getUserId(), event.getBookingId());
    }

    // Nghe sự kiện THANH TOÁN THẤT BẠI (Nếu muốn)
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "notification.fail.queue"),
            exchange = @Exchange(name = RabbitMQConfig.PAYMENT_EXCHANGE, type = ExchangeTypes.TOPIC),
            key = RabbitMQConfig.BOOKING_FAILED_KEY
    ))
    public void handlePaymentFail(PaymentResultEvent event) {
        log.warn("🔔 [NOTIFICATION] THÔNG BÁO: Booking #{} THẤT BẠI. Lý do: {}",
                event.getBookingId(), event.getMessage());
    }
}
