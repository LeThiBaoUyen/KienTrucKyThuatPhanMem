package com.flashsale.product.controller;

import com.flashsale.common.dto.ApiResponse;
import com.flashsale.common.model.Product;
import com.flashsale.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product Controller - PU1 API
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    /**
     * GET /api/products - Danh sách tất cả sản phẩm
     * ⚡ Load từ Redis - rất nhanh
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        long startTime = System.currentTimeMillis();
        log.info("📋 Receiving request: GET /api/products");

        List<Product> products = productService.getAllProducts();
        long duration = System.currentTimeMillis() - startTime;

        log.info("✅ Response time: {}ms", duration);
        return ResponseEntity.ok(ApiResponse.success("Found " + products.size() + " products", products));
    }

    /**
     * GET /api/products/{id} - Chi tiết 1 sản phẩm
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        long startTime = System.currentTimeMillis();
        log.info("📦 Receiving request: GET /api/products/{}", id);

        Product product = productService.getProductById(id);
        long duration = System.currentTimeMillis() - startTime;

        if (product == null) {
            log.warn("❌ Product {} not found", id);
            return ResponseEntity.ok(ApiResponse.notFound("Product not found"));
        }

        log.info("✅ Response time: {}ms", duration);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * POST /api/products/init - Khởi tạo sample products
     */
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<String>> initProducts() {
        log.info("🚀 Initializing sample products");
        productService.initializeSampleProducts();
        return ResponseEntity.ok(ApiResponse.success("Products initialized"));
    }

    /**
     * GET /api/products/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("PU1 Product Service is running ✅"));
    }
}
