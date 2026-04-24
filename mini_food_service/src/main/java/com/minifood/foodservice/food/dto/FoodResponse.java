package com.minifood.foodservice.food.dto;

public record FoodResponse(
        Long id,
        String name,
        Double price,
        String description,
        String image
) {
}
