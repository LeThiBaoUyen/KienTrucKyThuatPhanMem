package com.flashsale.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Stock/Inventory model - lưu trong Redis Data Grid
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long productId;
    private Integer quantity;
    private Long updatedAt;

    public Stock(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
        this.updatedAt = System.currentTimeMillis();
    }

    public synchronized void reduce(Integer amount) {
        this.quantity -= amount;
        this.updatedAt = System.currentTimeMillis();
    }

    public synchronized void increase(Integer amount) {
        this.quantity += amount;
        this.updatedAt = System.currentTimeMillis();
    }

    public boolean hasEnoughStock(Integer needed) {
        return this.quantity >= needed;
    }
}
