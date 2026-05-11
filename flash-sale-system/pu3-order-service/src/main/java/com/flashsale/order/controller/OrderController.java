package com.flashsale.order.controller;

import com.flashsale.common.dto.CheckoutRequest;
import com.flashsale.common.dto.ApiResponse;
import com.flashsale.common.model.Order;
import com.flashsale.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order Controller - PU3 API
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    /**
     * POST /api/orders/checkout - Đặt hàng
     * ⚡ Fast checkout - tất cả xử lý trong Redis
     */
    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<Order>> checkout(@RequestBody CheckoutRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("💳 POST /api/orders/checkout | User: {}", request.getUserId());

        try {
            Order order = orderService.checkout(request);
            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Response time: {}ms", duration);
            return ResponseEntity.ok(ApiResponse.success("Order created successfully", order));
        } catch (Exception e) {
            log.error("❌ Checkout failed: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * GET /api/orders/{orderId} - Lấy chi tiết đơn hàng
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable Long orderId) {
        log.info("🔍 GET /api/orders/{}", orderId);

        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            return ResponseEntity.ok(ApiResponse.notFound("Order not found"));
        }

        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * GET /api/orders/user/{userId} - Lấy tất cả đơn hàng của user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Order>>> getUserOrders(@PathVariable String userId) {
        log.info("📋 GET /api/orders/user/{}", userId);

        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(ApiResponse.success("Found " + orders.size() + " orders", orders));
    }

    /**
     * GET /api/orders/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("PU3 Order Service is running ✅"));
    }
}
