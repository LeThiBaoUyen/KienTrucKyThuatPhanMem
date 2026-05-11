package com.flashsale.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request giảm tồn kho
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReduceStockRequest {
    private Long productId;
    private Integer quantity;
}
