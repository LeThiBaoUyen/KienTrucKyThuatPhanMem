package com.flashsale.cart.controller;

import com.flashsale.common.dto.AddToCartRequest;
import com.flashsale.common.dto.ApiResponse;
import com.flashsale.common.model.Cart;
import com.flashsale.common.model.CartItem;
import com.flashsale.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Cart Controller - PU2 API
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;

    /**
     * GET /api/cart/{userId} - Lấy giỏ hàng
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<Cart>> getCart(@PathVariable String userId) {
        long startTime = System.currentTimeMillis();
        log.info("📋 GET /api/cart/{}", userId);

        Cart cart = cartService.getCart(userId);
        long duration = System.currentTimeMillis() - startTime;

        log.info("✅ Response time: {}ms", duration);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    /**
     * POST /api/cart/add - Thêm vào giỏ
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Cart>> addToCart(@RequestBody AddToCartRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("➕ POST /api/cart/add | User: {}, Product: {}, Qty: {}",
                request.getUserId(), request.getProductId(), request.getQuantity());

        CartItem item = new CartItem(
                request.getProductId(),
                request.getProductName(),
                request.getPrice(),
                request.getQuantity()
        );

        Cart cart = cartService.addToCart(request.getUserId(), item);
        long duration = System.currentTimeMillis() - startTime;

        log.info("✅ Response time: {}ms", duration);
        return ResponseEntity.ok(ApiResponse.success("Added to cart", cart));
    }

    /**
     * DELETE /api/cart/{userId}/item/{productId} - Xóa khỏi giỏ
     */
    @DeleteMapping("/{userId}/item/{productId}")
    public ResponseEntity<ApiResponse<Cart>> removeFromCart(
            @PathVariable String userId,
            @PathVariable Long productId) {
        log.info("➖ DELETE /api/cart/{}/item/{}", userId, productId);

        Cart cart = cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Removed from cart", cart));
    }

    /**
     * DELETE /api/cart/{userId} - Xóa toàn bộ giỏ
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<String>> clearCart(@PathVariable String userId) {
        log.info("🗑️ DELETE /api/cart/{}", userId);
        cartService.clearCart(userId);
        return ResponseEntity.ok(ApiResponse.success("Cart cleared"));
    }

    /**
     * GET /api/cart/{userId}/count - Số lượng items
     */
    @GetMapping("/{userId}/count")
    public ResponseEntity<ApiResponse<Integer>> getCartCount(@PathVariable String userId) {
        int count = cartService.getCartItemCount(userId);
        return ResponseEntity.ok(ApiResponse.success(count));
    }

    /**
     * GET /api/cart/{userId}/total - Tổng giá
     */
    @GetMapping("/{userId}/total")
    public ResponseEntity<ApiResponse<Double>> getCartTotal(@PathVariable String userId) {
        Double total = cartService.getCartTotal(userId);
        return ResponseEntity.ok(ApiResponse.success(total));
    }

    /**
     * GET /api/cart/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("PU2 Cart Service is running ✅"));
    }
}
