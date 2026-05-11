package com.flashsale.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request checkout/đặt hàng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private String userId;
    private String shippingAddress;
    private String phoneNumber;
}
