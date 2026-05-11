package com.flashsale.inventory.service;

import com.flashsale.common.model.Stock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Inventory Service - Quản lý tồn kho trong Redis Data Grid
 * ⚡ Tất cả xử lý trong memory - cực nhanh!
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
    private static final String STOCK_KEY_PREFIX = "stock:";
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new GsonBuilder().create();

    /**
     * Lấy tồn kho sản phẩm từ Redis
     */
    public Stock getStock(Long productId) {
        log.info("📦 Fetching stock for product: {}", productId);
        long startTime = System.currentTimeMillis();

        try {
            String key = STOCK_KEY_PREFIX + productId;
            Object value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                Stock stock = gson.fromJson(value.toString(), Stock.class);
                long duration = System.currentTimeMillis() - startTime;
                log.info("✅ Stock for product {}: {} units in {}ms", 
                        productId, stock.getQuantity(), duration);
                return stock;
            }

            log.warn("⚠️ Stock not found for product {}", productId);
            return null;
        } catch (Exception e) {
            log.error("❌ Error fetching stock", e);
            return null;
        }
    }

    /**
     * Giảm tồn kho
     * ⚡ Thực hiện trực tiếp trên Redis - không cần gọi DB
     */
    public Stock reduceStock(Long productId, Integer quantity) {
        log.info("➖ Reducing stock for product {} by {}", productId, quantity);
        long startTime = System.currentTimeMillis();

        try {
            String key = STOCK_KEY_PREFIX + productId;
            
            // Lấy stock hiện tại
            Stock stock = getStock(productId);
            if (stock == null) {
                log.error("❌ Product {} not found in stock", productId);
                throw new RuntimeException("Product not found");
            }

            // Kiểm tra đủ tồn kho
            if (!stock.hasEnoughStock(quantity)) {
                log.warn("❌ Insufficient stock for product {}", productId);
                throw new RuntimeException("Insufficient stock");
            }

            // Giảm tồn kho
            stock.reduce(quantity);

            // Lưu lại vào Redis
            String json = gson.toJson(stock);
            redisTemplate.opsForValue().set(key, json);

            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Stock reduced in {}ms | Product {}: {} units remaining", 
                    duration, productId, stock.getQuantity());

            return stock;
        } catch (Exception e) {
            log.error("❌ Error reducing stock", e);
            throw new RuntimeException("Error reducing stock: " + e.getMessage());
        }
    }

    /**
     * Tăng tồn kho (hoàn hàng)
     */
    public Stock increaseStock(Long productId, Integer quantity) {
        log.info("➕ Increasing stock for product {} by {}", productId, quantity);

        try {
            String key = STOCK_KEY_PREFIX + productId;
            Stock stock = getStock(productId);
            
            if (stock == null) {
                log.error("❌ Product {} not found", productId);
                throw new RuntimeException("Product not found");
            }

            stock.increase(quantity);

            String json = gson.toJson(stock);
            redisTemplate.opsForValue().set(key, json);

            log.info("✅ Stock increased | Product {}: {} units", 
                    productId, stock.getQuantity());

            return stock;
        } catch (Exception e) {
            log.error("❌ Error increasing stock", e);
            throw new RuntimeException("Error increasing stock: " + e.getMessage());
        }
    }

    /**
     * Khởi tạo stock cho các sản phẩm
     */
    public void initializeStock(Long productId, Integer quantity) {
        log.info("🚀 Initializing stock for product {}: {} units", productId, quantity);

        try {
            String key = STOCK_KEY_PREFIX + productId;
            Stock stock = new Stock(productId, quantity);
            String json = gson.toJson(stock);
            redisTemplate.opsForValue().set(key, json);
            log.info("✅ Stock initialized");
        } catch (Exception e) {
            log.error("❌ Error initializing stock", e);
        }
    }

    /**
     * Khởi tạo stock cho tất cả sản phẩm (sample)
     */
    public void initializeAllStocks() {
        log.info("🚀 Initializing stock for all products");

        // Khởi tạo stock cho 8 sản phẩm mẫu
        initializeStock(1L, 100);
        initializeStock(2L, 150);
        initializeStock(3L, 80);
        initializeStock(4L, 50);
        initializeStock(5L, 200);
        initializeStock(6L, 75);
        initializeStock(7L, 120);
        initializeStock(8L, 60);

        log.info("✅ All stocks initialized");
    }

    /**
     * Lấy số lượng stock
     */
    public Integer getStockQuantity(Long productId) {
        Stock stock = getStock(productId);
        return stock != null ? stock.getQuantity() : 0;
    }
}
