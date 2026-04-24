package com.minifood.foodservice.food.service;

import com.minifood.foodservice.common.exception.BusinessLogicException;
import com.minifood.foodservice.common.exception.NotFoundException;
import com.minifood.foodservice.food.dto.FoodRequest;
import com.minifood.foodservice.food.dto.FoodResponse;
import com.minifood.foodservice.food.dto.FoodUpdateRequest;
import com.minifood.foodservice.food.entity.Food;
import com.minifood.foodservice.food.repository.FoodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;

    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    @Override
    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public FoodResponse getFoodById(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mon an co ID " + id + " khong ton tai"));
        return toResponse(food);
    }

    @Override
    @Transactional
    public FoodResponse createFood(FoodRequest request) {
        if (foodRepository.existsByNameIgnoreCase(request.name())) {
            throw new BusinessLogicException("Mon an voi ten '" + request.name() + "' da ton tai");
        }

        Food food = new Food();
        food.setName(request.name().trim());
        food.setPrice(request.price());
        food.setDescription(request.description().trim());
        food.setImageUrl(request.image());

        return toResponse(foodRepository.save(food));
    }

    @Override
    @Transactional
    public FoodResponse updateFood(Long id, FoodUpdateRequest request) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mon an co ID " + id + " khong ton tai"));

        food.setName(request.name().trim());
        food.setPrice(request.price());
        food.setDescription(request.description().trim());
        if (request.image() != null && !request.image().isBlank()) {
            food.setImageUrl(request.image().trim());
        }

        return toResponse(foodRepository.save(food));
    }

    @Override
    @Transactional
    public void deleteFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Mon an co ID " + id + " khong ton tai"));
        foodRepository.delete(food);
    }

    private FoodResponse toResponse(Food food) {
        return new FoodResponse(
                food.getId(),
                food.getName(),
                food.getPrice(),
                food.getDescription(),
                food.getImageUrl()
        );
    }
}
