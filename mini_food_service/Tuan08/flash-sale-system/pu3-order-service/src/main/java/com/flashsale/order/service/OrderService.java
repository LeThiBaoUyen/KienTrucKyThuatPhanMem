package com.flashsale.order.service;

import com.flashsale.common.dto.CheckoutRequest;
import com.flashsale.common.model.Cart;
import com.flashsale.common.model.Order;
import com.flashsale.common.model.OrderItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Order Service - Xử lý checkout và tạo đơn hàng
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private static final String CART_KEY_PREFIX = "cart:";
    private static final String ORDER_KEY_PREFIX = "order:";
    private static final String USER_ORDERS_SET_KEY = "orders:";
    private static final long ORDER_ID_COUNTER_KEY_REDIS = 1000;
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new GsonBuilder().create();
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Thực hiện checkout
     * ⚡ Xử lý nhanh - tất cả trong Redis, không gọi DB
     */
    public Order checkout(CheckoutRequest request) {
        log.info("🛒 Checkout for user: {}", request.getUserId());
        long startTime = System.currentTimeMillis();

        try {
            // 1. Lấy cart từ Redis
            String cartKey = CART_KEY_PREFIX + request.getUserId();
            Object cartValue = redisTemplate.opsForValue().get(cartKey);

            if (cartValue == null) {
                log.warn("⚠️ Cart is empty for user {}", request.getUserId());
                throw new RuntimeException("Cart is empty");
            }

            Cart cart = gson.fromJson(cartValue.toString(), Cart.class);
            log.info("✅ Retrieved cart with {} items", cart.getItems().size());

            // 2. Validate stock trực tiếp tại Redis (không gọi inventory service)
            boolean stockValid = validateStock(cart);
            if (!stockValid) {
                log.warn("❌ Some products are out of stock");
                throw new RuntimeException("Out of stock");
            }

            // 3. Tạo Order
            Long orderId = generateOrderId();
            Order order = new Order(request.getUserId());
            order.setId(orderId);

            // Convert cart items to order items
            for (var item : cart.getItems()) {
                OrderItem orderItem = new OrderItem(
                        item.getProductId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity()
                );
                order.addItem(orderItem);
            }

            order.calculateTotal();
            order.setStatus("CONFIRMED");

            // 4. Lưu Order vào Redis
            String orderKey = ORDER_KEY_PREFIX + orderId;
            String orderJson = gson.toJson(order);
            redisTemplate.opsForValue().set(orderKey, orderJson);
            redisTemplate.expire(orderKey, java.time.Duration.ofDays(30));

            // 5. Thêm vào user's orders list
            String userOrdersKey = USER_ORDERS_SET_KEY + request.getUserId();
            redisTemplate.opsForSet().add(userOrdersKey, orderId);

            // 6. Call Inventory Service để giảm stock (async)
            // Có thể làm sync để đảm bảo
            reduceStock(cart);

            // 7. Xóa cart sau khi checkout thành công
            redisTemplate.delete(cartKey);

            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Checkout completed in {}ms | Order ID: {} | Total: ${}", 
                    duration, orderId, order.getTotalPrice());

            return order;
        } catch (Exception e) {
            log.error("❌ Checkout failed", e);
            throw new RuntimeException("Checkout failed: " + e.getMessage());
        }
    }

    /**
     * Lấy đơn hàng theo ID
     */
    public Order getOrderById(Long orderId) {
        log.info("🔍 Fetching order: {}", orderId);

        try {
            String key = ORDER_KEY_PREFIX + orderId;
            Object value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                Order order = gson.fromJson(value.toString(), Order.class);
                log.info("✅ Retrieved order {}", orderId);
                return order;
            }

            log.warn("⚠️ Order {} not found", orderId);
            return null;
        } catch (Exception e) {
            log.error("❌ Error fetching order", e);
            return null;
        }
    }

    /**
     * Lấy tất cả đơn hàng của user
     */
    public List<Order> getUserOrders(String userId) {
        log.info("📋 Fetching orders for user: {}", userId);

        try {
            String userOrdersKey = USER_ORDERS_SET_KEY + userId;
            Set<Object> orderIds = redisTemplate.opsForSet().members(userOrdersKey);
            List<Order> orders = new ArrayList<>();

            if (orderIds != null) {
                for (Object id : orderIds) {
                    Order order = getOrderById(((Number) id).longValue());
                    if (order != null) {
                        orders.add(order);
                    }
                }
            }

            log.info("✅ Retrieved {} orders", orders.size());
            return orders;
        } catch (Exception e) {
            log.error("❌ Error fetching user orders", e);
            return new ArrayList<>();
        }
    }

    /**
     * Validate stock - kiểm tra trực tiếp trong Redis
     */
    private boolean validateStock(Cart cart) {
        log.info("🔐 Validating stock for cart items");

        for (var item : cart.getItems()) {
            String stockKey = "stock:" + item.getProductId();
            Object stockValue = redisTemplate.opsForValue().get(stockKey);

            if (stockValue != null) {
                try {
                    // Kiểm tra xem có đủ tồn kho không
                    Integer stock = Integer.parseInt(stockValue.toString());
                    if (stock < item.getQuantity()) {
                        log.warn("⚠️ Product {} has insufficient stock", item.getProductId());
                        return false;
                    }
                } catch (NumberFormatException e) {
                    log.error("❌ Error parsing stock for product {}", item.getProductId());
                    return false;
                }
            } else {
                log.warn("⚠️ Stock info not found for product {}", item.getProductId());
                return false;
            }
        }

        log.info("✅ Stock validation passed");
        return true;
    }

    /**
     * Giảm tồn kho trực tiếp trong Redis
     */
    private void reduceStock(Cart cart) {
        log.info("📦 Reducing stock for {} items", cart.getItems().size());

        for (var item : cart.getItems()) {
            String stockKey = "stock:" + item.getProductId();
            Object stockValue = redisTemplate.opsForValue().get(stockKey);

            if (stockValue != null) {
                try {
                    Integer currentStock = Integer.parseInt(stockValue.toString());
                    Integer newStock = currentStock - item.getQuantity();
                    redisTemplate.opsForValue().set(stockKey, newStock.toString());
                    log.info("✓ Product {}: {} -> {}", item.getProductId(), currentStock, newStock);
                } catch (NumberFormatException e) {
                    log.error("❌ Error reducing stock", e);
                }
            }
        }

        log.info("✅ Stock reduced successfully");
    }

    /**
     * Generate Order ID
     */
    private Long generateOrderId() {
        // Đơn giản: dùng timestamp + random
        return System.currentTimeMillis() / 1000 + (long) (Math.random() * 10000);
    }
}
