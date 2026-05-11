package com.flashsale.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request thêm sản phẩm vào giỏ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    private String userId;
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
}
