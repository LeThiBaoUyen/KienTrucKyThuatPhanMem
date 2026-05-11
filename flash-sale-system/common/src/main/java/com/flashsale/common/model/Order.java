package com.flashsale.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Order model - lưu trong Redis + có thể vào Database sau
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String userId;
    private List<OrderItem> items;
    private Double totalPrice;
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED
    private Long createdAt;
    private Long updatedAt;

    public Order(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.status = "PENDING";
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void calculateTotal() {
        this.totalPrice = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
