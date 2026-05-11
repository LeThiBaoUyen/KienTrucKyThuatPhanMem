package com.flashsale.inventory.controller;

import com.flashsale.common.dto.ApiResponse;
import com.flashsale.common.dto.ReduceStockRequest;
import com.flashsale.common.model.Stock;
import com.flashsale.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Inventory Controller - PU4 API
 */
@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class InventoryController {
    private final InventoryService inventoryService;

    /**
     * GET /api/stock/{productId} - Lấy tồn kho
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Stock>> getStock(@PathVariable Long productId) {
        long startTime = System.currentTimeMillis();
        log.info("📦 GET /api/stock/{}", productId);

        Stock stock = inventoryService.getStock(productId);
        long duration = System.currentTimeMillis() - startTime;

        if (stock == null) {
            log.warn("❌ Stock not found for product {}", productId);
            return ResponseEntity.ok(ApiResponse.notFound("Stock not found"));
        }

        log.info("✅ Response time: {}ms", duration);
        return ResponseEntity.ok(ApiResponse.success(stock));
    }

    /**
     * POST /api/stock/reduce - Giảm tồn kho
     * ⚡ Xử lý nhanh - trực tiếp trên Redis
     */
    @PostMapping("/reduce")
    public ResponseEntity<ApiResponse<Stock>> reduceStock(@RequestBody ReduceStockRequest request) {
        long startTime = System.currentTimeMillis();
        log.info("➖ POST /api/stock/reduce | Product: {}, Qty: {}", 
                request.getProductId(), request.getQuantity());

        try {
            Stock stock = inventoryService.reduceStock(request.getProductId(), request.getQuantity());
            long duration = System.currentTimeMillis() - startTime;

            log.info("✅ Response time: {}ms", duration);
            return ResponseEntity.ok(ApiResponse.success("Stock reduced", stock));
        } catch (Exception e) {
            log.error("❌ Error reducing stock: {}", e.getMessage());
            return ResponseEntity.ok(ApiResponse.error(400, e.getMessage()));
        }
    }

    /**
     * POST /api/stock/init - Khởi tạo stock
     */
    @PostMapping("/init")
    public ResponseEntity<ApiResponse<String>> initStock() {
        log.info("🚀 Initializing stock");
        inventoryService.initializeAllStocks();
        return ResponseEntity.ok(ApiResponse.success("Stock initialized"));
    }

    /**
     * GET /api/stock/{productId}/quantity - Lấy số lượng
     */
    @GetMapping("/{productId}/quantity")
    public ResponseEntity<ApiResponse<Integer>> getQuantity(@PathVariable Long productId) {
        Integer quantity = inventoryService.getStockQuantity(productId);
        return ResponseEntity.ok(ApiResponse.success(quantity));
    }

    /**
     * GET /api/stock/health - Health check
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("PU4 Inventory Service is running ✅"));
    }
}
