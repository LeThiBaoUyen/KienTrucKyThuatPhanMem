package com.flashsale.cart.service;

import com.flashsale.common.model.Cart;
import com.flashsale.common.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Cart Service - Quản lý giỏ hàng trong Redis Data Grid
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private static final String CART_KEY_PREFIX = "cart:";
    private static final long CART_EXPIRATION_SECONDS = 86400; // 24h
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new GsonBuilder().create();

    /**
     * Lấy giỏ hàng của user từ Redis
     */
    public Cart getCart(String userId) {
        log.info("🛒 Fetching cart for user: {}", userId);
        long startTime = System.currentTimeMillis();

        try {
            String key = CART_KEY_PREFIX + userId;
            Object value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                Cart cart = gson.fromJson(value.toString(), Cart.class);
                long duration = System.currentTimeMillis() - startTime;
                log.info("✅ Retrieved cart in {}ms | Items: {}", duration, cart.getItems().size());
                return cart;
            }

            // Tạo cart mới nếu không có
            Cart newCart = new Cart(userId);
            log.info("📝 Creating new cart for user: {}", userId);
            return newCart;
        } catch (Exception e) {
            log.error("❌ Error fetching cart", e);
            return new Cart(userId);
        }
    }

    /**
     * Thêm sản phẩm vào giỏ
     * ⚡ Xử lý trực tiếp trong Redis - không gọi DB
     */
    public Cart addToCart(String userId, CartItem item) {
        log.info("➕ Adding product {} to cart for user {}", item.getProductId(), userId);
        long startTime = System.currentTimeMillis();

        try {
            // Lấy cart từ Redis
            String key = CART_KEY_PREFIX + userId;
            Cart cart = getCart(userId);

            // Thêm item
            cart.addItem(item);

            // Lưu lại vào Redis
            String json = gson.toJson(cart);
            redisTemplate.opsForValue().set(key, json);
            redisTemplate.expire(key, java.time.Duration.ofSeconds(CART_EXPIRATION_SECONDS));

            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Added to cart in {}ms | Total items: {}", duration, cart.getItems().size());

            return cart;
        } catch (Exception e) {
            log.error("❌ Error adding to cart", e);
            throw new RuntimeException("Error adding to cart", e);
        }
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    public Cart removeFromCart(String userId, Long productId) {
        log.info("➖ Removing product {} from cart for user {}", productId, userId);

        try {
            String key = CART_KEY_PREFIX + userId;
            Cart cart = getCart(userId);

            cart.removeItem(productId);

            String json = gson.toJson(cart);
            redisTemplate.opsForValue().set(key, json);
            redisTemplate.expire(key, java.time.Duration.ofSeconds(CART_EXPIRATION_SECONDS));

            log.info("✅ Removed from cart | Remaining items: {}", cart.getItems().size());
            return cart;
        } catch (Exception e) {
            log.error("❌ Error removing from cart", e);
            throw new RuntimeException("Error removing from cart", e);
        }
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public void clearCart(String userId) {
        log.info("🗑️ Clearing cart for user {}", userId);

        try {
            String key = CART_KEY_PREFIX + userId;
            redisTemplate.delete(key);
            log.info("✅ Cart cleared");
        } catch (Exception e) {
            log.error("❌ Error clearing cart", e);
            throw new RuntimeException("Error clearing cart", e);
        }
    }

    /**
     * Lấy tổng số lượng items trong giỏ
     */
    public int getCartItemCount(String userId) {
        Cart cart = getCart(userId);
        return cart.getItems().size();
    }

    /**
     * Lấy tổng giá giỏ hàng
     */
    public Double getCartTotal(String userId) {
        Cart cart = getCart(userId);
        return cart.getTotalPrice();
    }
}
