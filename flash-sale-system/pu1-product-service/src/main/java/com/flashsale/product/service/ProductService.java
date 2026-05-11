package com.flashsale.product.service;

import com.flashsale.common.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Product Service - Load từ Redis Data Grid
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private static final String PRODUCT_KEY_PREFIX = "product:";
    private static final String PRODUCTS_SET_KEY = "products:all";
    private final RedisTemplate<String, Object> redisTemplate;
    private final Gson gson = new GsonBuilder().create();

    /**
     * Lấy tất cả sản phẩm từ Redis
     * ⚡ Xử lý nhanh - tất cả dữ liệu đã ở trong memory
     */
    public List<Product> getAllProducts() {
        log.info("📦 Fetching all products from Redis Data Grid");
        long startTime = System.currentTimeMillis();

        try {
            Set<Object> productIds = redisTemplate.opsForSet().members(PRODUCTS_SET_KEY);
            List<Product> products = new ArrayList<>();

            if (productIds != null && !productIds.isEmpty()) {
                for (Object id : productIds) {
                    String key = PRODUCT_KEY_PREFIX + id;
                    Object value = redisTemplate.opsForValue().get(key);
                    if (value != null) {
                        Product product = gson.fromJson(value.toString(), Product.class);
                        products.add(product);
                    }
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("✅ Retrieved {} products in {}ms", products.size(), duration);
            return products;
        } catch (Exception e) {
            log.error("❌ Error fetching products from Redis", e);
            return new ArrayList<>();
        }
    }

    /**
     * Lấy chi tiết 1 sản phẩm từ Redis
     */
    public Product getProductById(Long productId) {
        log.info("🔍 Fetching product {} from Redis", productId);
        long startTime = System.currentTimeMillis();

        try {
            String key = PRODUCT_KEY_PREFIX + productId;
            Object value = redisTemplate.opsForValue().get(key);

            if (value != null) {
                Product product = gson.fromJson(value.toString(), Product.class);
                long duration = System.currentTimeMillis() - startTime;
                log.info("✅ Retrieved product {} in {}ms", productId, duration);
                return product;
            }

            log.warn("⚠️ Product {} not found", productId);
            return null;
        } catch (Exception e) {
            log.error("❌ Error fetching product {}", productId, e);
            return null;
        }
    }

    /**
     * Khởi tạo sample products trong Redis
     * (Chỉ chạy lần đầu tiên)
     */
    public void initializeSampleProducts() {
        log.info("🚀 Initializing sample products in Redis");

        List<Product> sampleProducts = List.of(
                new Product(1L, "iPhone 15 Pro", 1299.99, 100),
                new Product(2L, "Samsung Galaxy S24", 999.99, 150),
                new Product(3L, "iPad Air", 799.99, 80),
                new Product(4L, "MacBook Pro 16", 2499.99, 50),
                new Product(5L, "AirPods Pro", 249.99, 200),
                new Product(6L, "Apple Watch Ultra", 799.99, 75),
                new Product(7L, "Sony WH-1000XM5", 399.99, 120),
                new Product(8L, "Dell XPS 15", 1899.99, 60)
        );

        for (Product product : sampleProducts) {
            String key = PRODUCT_KEY_PREFIX + product.getId();
            String json = gson.toJson(product);
            redisTemplate.opsForValue().set(key, json);
            redisTemplate.opsForSet().add(PRODUCTS_SET_KEY, product.getId());
            log.debug("✓ Added product: {}", product.getName());
        }

        log.info("✅ {} sample products initialized in Redis", sampleProducts.size());
    }

    /**
     * Kiểm tra sản phẩm tồn tại
     */
    public boolean productExists(Long productId) {
        String key = PRODUCT_KEY_PREFIX + productId;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
