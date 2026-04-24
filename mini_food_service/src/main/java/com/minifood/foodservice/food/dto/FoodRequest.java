package com.minifood.foodservice.food.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FoodRequest(
        @NotBlank(message = "name khong duoc de trong")
        @Size(max = 120, message = "name toi da 120 ky tu")
        String name,

        @NotNull(message = "price khong duoc de trong")
        @DecimalMin(value = "0.0", inclusive = false, message = "price phai lon hon 0")
        Double price,

        @NotBlank(message = "description khong duoc de trong")
        @Size(max = 500, message = "description toi da 500 ky tu")
        String description,

        @NotBlank(message = "image khong duoc de trong")
        String image
) {
}
