package com.minifood.foodservice.food.service;

import com.minifood.foodservice.food.dto.FoodRequest;
import com.minifood.foodservice.food.dto.FoodResponse;
import com.minifood.foodservice.food.dto.FoodUpdateRequest;

import java.util.List;

public interface FoodService {
    List<FoodResponse> getAllFoods();

    FoodResponse getFoodById(Long id);

    FoodResponse createFood(FoodRequest request);

    FoodResponse updateFood(Long id, FoodUpdateRequest request);

    void deleteFood(Long id);
}
