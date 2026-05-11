package com.flashsale.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Shopping Cart model - lưu trong Redis Data Grid
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private List<CartItem> items;
    private Long createdAt;
    private Long updatedAt;

    public Cart(String userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public void addItem(CartItem item) {
        // Kiểm tra sản phẩm đã có trong giỏ chưa
        boolean found = false;
        for (CartItem existing : items) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(item);
        }
        this.updatedAt = System.currentTimeMillis();
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProductId().equals(productId));
        this.updatedAt = System.currentTimeMillis();
    }

    public Double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public void clear() {
        items.clear();
        this.updatedAt = System.currentTimeMillis();
    }
}
