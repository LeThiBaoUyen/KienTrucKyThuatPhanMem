package com.minifood.foodservice.food.controller;

import com.minifood.foodservice.common.api.ApiResponse;
import com.minifood.foodservice.common.exception.ServiceUnavailableException;
import com.minifood.foodservice.common.security.RoleGuard;
import com.minifood.foodservice.food.dto.FoodRequest;
import com.minifood.foodservice.food.dto.FoodResponse;
import com.minifood.foodservice.food.dto.FoodUpdateRequest;
import com.minifood.foodservice.food.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;
    private final RoleGuard roleGuard;

    public FoodController(FoodService foodService, RoleGuard roleGuard) {
        this.foodService = foodService;
        this.roleGuard = roleGuard;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FoodResponse>>> getAllFoods() {
        List<FoodResponse> data = foodService.getAllFoods();
        return ResponseEntity.ok(ApiResponse.ok(200, "Lay danh sach mon an thanh cong", data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodResponse>> getFoodById(
            @PathVariable Long id,
            @RequestParam(name = "simulateServiceDown", required = false, defaultValue = "false") boolean simulateServiceDown
    ) {
        if (simulateServiceDown) {
            throw new ServiceUnavailableException("Dich vu lay thong tin mon an dang tam gian doan. Vui long thu lai sau.");
        }

        FoodResponse data = foodService.getFoodById(id);
        return ResponseEntity.ok(ApiResponse.ok(200, "Lay chi tiet mon an thanh cong", data));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FoodResponse>> createFood(
            @RequestHeader(name = "X-ROLE", required = false) String role,
            @Valid @RequestBody FoodRequest request
    ) {
        roleGuard.requireAdmin(role);
        FoodResponse data = foodService.createFood(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(201, "Tao mon an thanh cong", data));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FoodResponse>> updateFood(
            @RequestHeader(name = "X-ROLE", required = false) String role,
            @PathVariable Long id,
            @Valid @RequestBody FoodUpdateRequest request
    ) {
        roleGuard.requireAdmin(role);
        FoodResponse data = foodService.updateFood(id, request);
        return ResponseEntity.ok(ApiResponse.ok(200, "Cap nhat mon an thanh cong", data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(
            @RequestHeader(name = "X-ROLE", required = false) String role,
            @PathVariable Long id
    ) {
        roleGuard.requireAdmin(role);
        foodService.deleteFood(id);
        return ResponseEntity.ok(ApiResponse.ok(200, "Xoa mon an thanh cong", null));
    }
}
